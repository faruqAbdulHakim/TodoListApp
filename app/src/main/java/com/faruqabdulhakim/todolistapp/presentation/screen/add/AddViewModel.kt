package com.faruqabdulhakim.todolistapp.presentation.screen.add

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faruqabdulhakim.todolistapp.domain.model.Todo
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository
import com.faruqabdulhakim.todolistapp.domain.usecase.ValidateInput
import com.faruqabdulhakim.todolistapp.domain.usecase.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    @Named("titleInput") private val validateTitleInput: ValidateInput
) : ViewModel() {

    var state by mutableStateOf(AddTodoFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun dispatchEvent(event: AddTodoFormEvent) {
        when (event) {
            is AddTodoFormEvent.TitleChanged -> {
                state = state.copy(title = event.title, titleError = null)
            }
            is AddTodoFormEvent.DescriptionChanged -> {
                state = state.copy(description = event.description)
            }
            is AddTodoFormEvent.TodoStatusChanged -> {
                state = state.copy(isDone = event.isDone)
            }
            is AddTodoFormEvent.Submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        val titleValidation = validateTitleInput.validate(state.title)

        val hasError = listOf(
            titleValidation
        ).any { validationResult -> validationResult is ValidationResult.Error  }

        if (titleValidation is ValidationResult.Error) {
            state = state.copy(titleError = titleValidation.errorMessage)
        }

        viewModelScope.launch {
            if (hasError) {
                validationEventChannel.send(ValidationEvent.Error)
            } else {
                val newTodo = Todo(
                    title = state.title,
                    description = state.description,
                    isDone = state.isDone
                )
                todoRepository.addTodo(newTodo)
                validationEventChannel.send(ValidationEvent.Success)
            }

        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
        object Error: ValidationEvent()
    }
}