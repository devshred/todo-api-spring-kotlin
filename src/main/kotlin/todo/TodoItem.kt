package todo

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("TODO")
data class TodoItem(@Id val id: String?, val text: String?, var done: Boolean)
