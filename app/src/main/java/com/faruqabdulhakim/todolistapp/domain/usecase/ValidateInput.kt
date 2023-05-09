package com.faruqabdulhakim.todolistapp.domain.usecase

interface ValidateInput {
    fun validate(text: String) : ValidationResult
}