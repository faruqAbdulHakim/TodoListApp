package com.faruqabdulhakim.todolistapp.presentation.di

import android.content.Context
import com.faruqabdulhakim.todolistapp.App
import com.faruqabdulhakim.todolistapp.data.local.TodoDatabase
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository

object Injection {
    fun provideTodoRepository(context: Context): TodoRepository {
        val todoDatabase = TodoDatabase.getInstance(context, App.getApplicationScope())
        val todoDao = todoDatabase.todoDao()
        return TodoRepository.getInstance(todoDao = todoDao)
    }
}