package com.faruqabdulhakim.todolistapp.domain.repository

import com.faruqabdulhakim.todolistapp.data.local.TodoDao
import com.faruqabdulhakim.todolistapp.data.model.Todo

class TodoRepository private constructor(
    private val todoDao: TodoDao
) {

    fun getAllTodos(query: String = "") = todoDao.getAllTodos(query)
    fun getTodoById(id: Int) = todoDao.getTodoById(id)
    suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo)
    suspend fun addTodo(todo: Todo) = todoDao.insert(todo)
    suspend fun updateTodo(todo: Todo) = todoDao.update(todo)

    companion object {
        @Volatile
        var INSTANCE: TodoRepository? = null

        fun getInstance(todoDao: TodoDao): TodoRepository {
            if (INSTANCE == null) {
                INSTANCE = synchronized(this) {
                    TodoRepository(todoDao)
                }
            }
            return INSTANCE as TodoRepository
        }
    }
}