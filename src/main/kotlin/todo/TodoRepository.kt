package todo

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface TodoRepository : CrudRepository<TodoItem, String> {
    @Query("SELECT * FROM todo")
    fun allTodoItems(): List<TodoItem>
}
