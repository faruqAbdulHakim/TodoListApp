package com.faruqabdulhakim.todolistapp.data.repository

import com.faruqabdulhakim.todolistapp.data.local.TodoDao
import com.faruqabdulhakim.todolistapp.domain.model.Todo
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository

class TodoRepositoryImpl constructor(
    private val todoDao: TodoDao
) : TodoRepository {

    override fun getAllTodos(query: String) = todoDao.getAllTodos(query)

    override fun getTodoById(id: Int) = todoDao.getTodoById(id)

    override suspend fun deleteTodo(todo: Todo) = todoDao.delete(todo)

    override suspend fun addTodo(todo: Todo) = todoDao.insert(todo)

    override suspend fun updateTodo(todo: Todo) = todoDao.update(todo)
}