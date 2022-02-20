package todo

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service
import todo.model.CreateTodoItem
import todo.model.TodoItem
import todo.model.TodoStatus
import java.util.UUID

@Service
class TodoService(val db: TodoRepository) {
    fun allTodoItems(): List<TodoItem> = db.allTodoItems().map { i -> i.toTodoItem() }

    fun save(todoItem: CreateTodoItem): TodoItem {
        return db.save(todoItem.toEntity()).toTodoItem()
    }

    fun findById(id: UUID): Either<BadState, TodoItem> {
        val optional = db.findById(id.toString())
        return if (optional.isPresent) {
            optional.get().toTodoItem().right()
        } else {
            BadState.NotFound("todo item not found").left()
        }
    }

    fun updateStatus(id: UUID, todoStatus: TodoStatus): Either<BadState, GoodState> {
        val optional = db.findById(id.toString())
        return if (optional.isPresent) {
            val dbObject = optional.get()
            dbObject.done = todoStatus.done
            db.save(dbObject)
            GoodState.Updated("updated item $id").right()
        } else {
            BadState.NotFound("todo item not found").left()
        }
    }

    fun delete(id: UUID): Either<BadState, GoodState> {
        val optional = db.findById(id.toString())
        return if (optional.isPresent) {
            db.delete(optional.get())
            GoodState.Deleted("deleted item $id").right()
        } else {
            BadState.NotFound("todo item not found").left()
        }
    }
}

sealed class BadState {
    data class NotFound(val reason: String) : BadState()
}

sealed class GoodState {
    data class Updated(val reason: String) : GoodState()
    data class Deleted(val reason: String) : GoodState()
}

fun CreateTodoItem.toEntity(): TodoEntity = TodoEntity(
    id = null,
    text = this.text,
    done = false
)