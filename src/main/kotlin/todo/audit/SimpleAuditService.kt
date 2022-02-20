package todo.audit

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Service
import java.net.InetAddress

@Service
@ConditionalOnExpression("T(org.springframework.util.StringUtils).isEmpty('\${spring.kafka.bootstrap-servers:}')")
class SimpleAuditService : ApplicationListener<AuditApplicationEvent> {
    private val logger = KotlinLogging.logger {}

    override fun onApplicationEvent(event: AuditApplicationEvent) {
        val auditEvent = AuditEvent(
            host = InetAddress.getLocalHost().hostName,
            action = event.action,
            todoEntity = event.todoEntity
        )
        logger.info(auditEvent.toString())
    }
}
