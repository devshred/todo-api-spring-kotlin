package todo.audit

import todo.TodoEntity

data class AuditEvent(var host: String?, val action: Action, val todoEntity: TodoEntity)

enum class Action {
    INSERT, UPDATE, DELETE
}
