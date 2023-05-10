package com.faruqabdulhakim.todolistapp.domain.repository

import com.faruqabdulhakim.todolistapp.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getAllTodos(query: String): Flow<List<Todo>>
    fun getTodoById(id: Int): Flow<List<Todo>>
    suspend fun deleteTodo(todo: Todo)
    suspend fun addTodo(todo: Todo)
    suspend fun updateTodo(todo: Todo)
}
