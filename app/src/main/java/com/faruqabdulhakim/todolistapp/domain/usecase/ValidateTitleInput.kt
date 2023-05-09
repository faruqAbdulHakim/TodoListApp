package com.faruqabdulhakim.todolistapp.domain.usecase

import com.faruqabdulhakim.todolistapp.App
import com.faruqabdulhakim.todolistapp.R

class ValidateTitleInput : ValidateInput {

    override fun validate(text: String): ValidationResult =
        if (text.trim().isEmpty()) ValidationResult.Error(App.getResources().getString(R.string.input_error_blank))
        else ValidationResult.Success

}