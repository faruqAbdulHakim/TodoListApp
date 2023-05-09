package com.faruqabdulhakim.todolistapp.presentation.screen.add

data class AddTodoFormState(
    val title: String = "",
    val titleError: String? = null,
    val description: String = "",
    val isDone: Boolean = false
)
