package todo.audit

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationListener
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service
import java.net.InetAddress

@Service
@EnableKafka
@ConditionalOnProperty("spring.kafka.bootstrap-servers")
class KafkaAuditService(private val kafkaTemplate: KafkaTemplate<String, AuditEvent>) :
    ApplicationListener<AuditApplicationEvent> {

    override fun onApplicationEvent(event: AuditApplicationEvent) {
        val auditEvent = AuditEvent(
            host = InetAddress.getLocalHost().hostName,
            action = event.action,
            todoEntity = event.todoEntity
        )
        kafkaTemplate.send(kafkaTemplate.defaultTopic, auditEvent.todoEntity.id.toString(), auditEvent)
    }
}
