package com.faruqabdulhakim.todolistapp.presentation.screen.add

sealed class AddTodoFormEvent private constructor() {
    data class TitleChanged(val title: String) : AddTodoFormEvent()
    data class DescriptionChanged(val description: String) : AddTodoFormEvent()
    data class TodoStatusChanged(val isDone: Boolean) : AddTodoFormEvent()
    object Submit : AddTodoFormEvent()
}
