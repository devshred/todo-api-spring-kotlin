import com.palantir.gradle.docker.DockerExtension
import com.palantir.gradle.docker.DockerRunExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask
import org.springframework.boot.gradle.tasks.bundling.BootJar

val springdocVersion: String = "1.6.6"

plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    val kotlinVersion = "1.6.10"
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion

    id("com.palantir.docker") version "0.32.0"
    id("com.palantir.docker-run") version "0.32.0"
    id("org.openapi.generator") version "5.4.0"
    id("com.github.ben-manes.versions") version "0.42.0"
    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
}

group = "todo"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springdoc:springdoc-openapi-data-rest:$springdocVersion")
    implementation("org.springdoc:springdoc-openapi-ui:$springdocVersion")
    implementation("org.springdoc:springdoc-openapi-kotlin:$springdocVersion")
    implementation("io.arrow-kt:arrow-core:1.0.1")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val bootJar: BootJar by tasks
val projectName = project.name
val dockerImageName = projectName

configure<DockerExtension> {
    name = dockerImageName
    tag("latest", "latest")

    buildArgs(mapOf("JAR_FILE" to bootJar.archiveFileName.get()))
    setDockerfile(file("src/main/docker/Dockerfile"))
    files(bootJar.archiveFile)
    pull(true)
}

configure<DockerRunExtension> {
    name = projectName
    image = dockerImageName
    daemonize = false
    clean = true
    ports("7001:8080")
}

val oasPackage = "todo"
val oasSpecLocation = "src/main/resources/todo-spec.yaml"
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")

val generateApiSpec = tasks.register("generateApiSpec", GenerateTask::class) {
    input = project.file(oasSpecLocation).path
    outputDir.set(oasGenOutputDir.get().toString())
    modelPackage.set("$oasPackage.model")
    apiPackage.set("$oasPackage.api")
    packageName.set(oasPackage)
    generatorName.set("kotlin-spring")
    configOptions.set(
        mapOf(
            "dateLibrary" to "java8",
            "interfaceOnly" to "true",
            "useTags" to "true",
            "openApiNullable" to "false"
        )
    )
}

tasks.named("compileKotlin") {
    dependsOn(generateApiSpec)
}

java.sourceSets["main"].java {
    srcDir("${oasGenOutputDir.get()}/src/main/kotlin")
}

tasks {
    dependencyUpdates {
        resolutionStrategy {
            componentSelection {
                all {
                    val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea")
                        .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-+]*") }
                        .any { it.matches(candidate.version) }
                    if (rejected) {
                        reject("Release candidate")
                    }
                }
            }
        }
    }
}

ktlint {
    version.set("0.44.0")
    enableExperimentalRules.set(true)
    reporters {
        reporter(CHECKSTYLE)
    }
    filter {
        exclude("**/todo/api/**")
        exclude("**/todo/model/**")
    }
}
