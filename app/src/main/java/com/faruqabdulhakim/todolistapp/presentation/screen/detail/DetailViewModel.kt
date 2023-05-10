package com.faruqabdulhakim.todolistapp.presentation.screen.detail

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
class DetailViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {

    private var _todo = MutableStateFlow<Todo?>(null)
    val todo get() = _todo.asStateFlow()

    fun getTodoDetail(todoId: Int) {
        viewModelScope.launch {
            todoRepository.getTodoById(todoId).collect { todoList ->
                _todo.update { todoList.firstOrNull() }
            }
        }
    }

    fun toggleTodoStatus(todo: Todo) {
        viewModelScope.launch {
            todoRepository.updateTodo(todo.copy(isDone = !todo.isDone))
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoRepository.deleteTodo(todo)
        }
    }
}