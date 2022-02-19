package todo

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import org.springframework.stereotype.Service

@Service
class TodoService(val db: TodoRepository) {
    fun allTodoItems(): List<TodoItem> = db.allTodoItems()

    fun save(todoItem: TodoItem): TodoItem {
        return db.save(todoItem.apply { done = false })
    }

    fun findById(id: String): Either<BadState, TodoItem> {
        val optional = db.findById(id)
        return if (optional.isPresent) {
            optional.get().right()
        } else {
            BadState.NotFound("todo item not found").left()
        }
    }

    fun update(id: String, patchedObject: TodoItem): Either<BadState, GoodState> {
        val optional = db.findById(id)
        return if (optional.isPresent) {
            val dbObject = optional.get()
            dbObject.done = patchedObject.done
            db.save(dbObject)
            GoodState.Updated("updated item $id").right()
        } else {
            BadState.NotFound("todo item not found").left()
        }
    }

    fun delete(id: String): Either<BadState, GoodState> {
        val optional = db.findById(id)
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
