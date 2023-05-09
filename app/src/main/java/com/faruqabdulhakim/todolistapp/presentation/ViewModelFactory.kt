package com.faruqabdulhakim.todolistapp.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository
import com.faruqabdulhakim.todolistapp.presentation.di.Injection
import com.faruqabdulhakim.todolistapp.presentation.screen.add.AddViewModel
import com.faruqabdulhakim.todolistapp.presentation.screen.detail.DetailViewModel
import com.faruqabdulhakim.todolistapp.presentation.screen.home.HomeViewModel

class ViewModelFactory private constructor(
    val todoRepository: TodoRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(todoRepository) as T
        } else if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(todoRepository) as T
        } else if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }

    companion object {
        @Volatile
        var INSTANCE: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                INSTANCE = synchronized(this) {
                    ViewModelFactory(
                        todoRepository = Injection.provideTodoRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}