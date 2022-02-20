package todo.audit

import org.springframework.context.ApplicationEvent
import todo.TodoEntity

class AuditApplicationEvent(source: Any, val action: Action, val todoEntity: TodoEntity) : ApplicationEvent(source)
