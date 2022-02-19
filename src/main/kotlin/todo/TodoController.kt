package todo

import arrow.core.Either
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api")
class TodoController(val service: TodoService) {
    @GetMapping("/")
    fun allTodoItems(): ResponseEntity<List<TodoItem>> = ResponseEntity.ok(service.allTodoItems())

    @PostMapping
    fun createTodoItem(@RequestBody todoItem: TodoItem): ResponseEntity<TodoItem> {
        val savedTodoItem = service.save(todoItem)
        return ResponseEntity
            .created(URI.create("/${savedTodoItem.id}"))
            .body(savedTodoItem)
    }

    @GetMapping("/{id}")
    fun getTodoItem(@PathVariable id: String): ResponseEntity<TodoItem> {
        return when (val todoItemOptional = service.findById(id)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.ok(todoItemOptional.value)
        }
    }

    @PatchMapping("/{id}")
    fun changeTodoItem(@PathVariable id: String, @RequestBody todoItem: TodoItem): ResponseEntity<String> {
        return when (service.update(id, todoItem)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.noContent().build()
        }
    }

    @DeleteMapping("/{id}")
    fun deleteTodoItem(@PathVariable id: String): ResponseEntity<String> {
        return when (service.delete(id)) {
            is Either.Left -> ResponseEntity.notFound().build()
            is Either.Right -> ResponseEntity.noContent().build()
        }
    }
}
