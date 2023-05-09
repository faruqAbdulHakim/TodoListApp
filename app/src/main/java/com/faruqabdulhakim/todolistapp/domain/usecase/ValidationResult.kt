package com.faruqabdulhakim.todolistapp.domain.usecase

sealed class ValidationResult private constructor() {
    data class Error(val errorMessage: String): ValidationResult()
    object Success: ValidationResult()
}
