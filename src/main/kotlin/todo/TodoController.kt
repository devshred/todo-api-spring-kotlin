package todo

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import todo.api.TodoApi
import todo.model.CreateTodoItem
import todo.model.TodoItem
import todo.model.TodoStatus
import java.net.URI
import java.util.UUID

@RestController
class TodoController(val service: TodoService) : TodoApi {
    override fun allTodoItems(): ResponseEntity<List<TodoItem>> = ResponseEntity.ok(service.allTodoItems())

    override fun createTodoItem(@RequestBody createTodoItem: CreateTodoItem): ResponseEntity<TodoItem> {
        val savedTodoItem = service.save(createTodoItem)
        return ResponseEntity
            .created(URI.create("/${savedTodoItem.id}"))
            .body(savedTodoItem)
    }

    override fun getTodoItem(@PathVariable id: UUID): ResponseEntity<TodoItem> {
        return when (val todoItemOptional = service.findById(id)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.ok(todoItemOptional.value)
        }
    }

    override fun changeTodoItem(@PathVariable id: UUID, @RequestBody todoStatus: TodoStatus): ResponseEntity<Unit> {
        return when (service.updateStatus(id, todoStatus)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.noContent().build()
        }
    }

    override fun deleteTodoItem(@PathVariable id: UUID): ResponseEntity<Unit> {
        return when (service.delete(id)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.noContent().build()
        }
    }
}
