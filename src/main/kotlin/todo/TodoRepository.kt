package todo

import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository

interface TodoRepository : CrudRepository<TodoItem, String>{
    @Query("select * from todo")
    fun allTodoItems(): List<TodoItem>
}
