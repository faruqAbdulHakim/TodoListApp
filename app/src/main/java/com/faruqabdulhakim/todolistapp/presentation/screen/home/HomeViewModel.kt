package com.faruqabdulhakim.todolistapp.presentation.screen.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faruqabdulhakim.todolistapp.domain.model.Todo
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private var _todoList = MutableStateFlow<Map<Boolean, List<Todo>>>(emptyMap())
    val todoList get() = _todoList.asStateFlow()

    private var _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getAllTodo() {
        updateTodoList()
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo)
        }
    }

    fun searchTodo(searchQuery: String) {
        _query.value = searchQuery
        updateTodoList()
    }

    private fun updateTodoList() {
        viewModelScope.launch {
            todoRepository.getAllTodos(query.value).collect {
                Log.d("HomeViewModel", it.toString())
                val groupedTodoList = it.groupBy { todo ->
                    todo.isDone
                }.toMutableMap()

                if (!groupedTodoList.containsKey(true)) {
                    groupedTodoList[true] = emptyList()
                }
                if (!groupedTodoList.containsKey(false)) {
                    groupedTodoList[false] = emptyList()
                }

                _todoList.update { groupedTodoList }
            }
        }
    }
}