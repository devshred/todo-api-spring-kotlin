package todo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import todo.model.TodoItem
import java.util.UUID

@Table("TODO")
data class TodoEntity(@Id val id: String?, val text: String, var done: Boolean) {
    fun toTodoItem(): TodoItem = TodoItem(id = UUID.fromString(this.id), text = this.text, done = this.done)
}
