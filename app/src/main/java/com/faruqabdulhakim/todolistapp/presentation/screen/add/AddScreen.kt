package com.faruqabdulhakim.todolistapp.presentation.screen.add

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faruqabdulhakim.todolistapp.R
import com.faruqabdulhakim.todolistapp.ui.theme.TodoListAppTheme

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    onAddTodoSuccess: () -> Unit = {},
) {
    val viewModel = hiltViewModel<AddViewModel>()
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.add_todo),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
        )
        AddTodoForm(
            viewModel = viewModel,
            onAddTodoSuccess = onAddTodoSuccess,
        )
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DefaultPreview() {
    TodoListAppTheme {
        AddScreen()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoForm(
    modifier: Modifier = Modifier,
    viewModel: AddViewModel,
    onAddTodoSuccess: () -> Unit = {}
) {
    val formState = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { validationEvent ->
            when (validationEvent) {
                is AddViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.add_todo_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    onAddTodoSuccess()
                }
                is AddViewModel.ValidationEvent.Error -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.add_todo_fail),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = formState.title,
            label = { Text(stringResource(R.string.title)) },
            placeholder = { Text(stringResource(R.string.input_title_instruction)) },
            onValueChange = { newTitle ->
                viewModel.dispatchEvent(AddTodoFormEvent.TitleChanged(newTitle))
            },
            isError = formState.titleError != null,
            supportingText =  {
                if (formState.titleError != null) Text(formState.titleError)
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_todo_title")
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = formState.description,
            label = { Text(stringResource(R.string.description_optional)) },
            placeholder = { Text(stringResource(R.string.input_description_instruction)) },
            onValueChange = { newDescription ->
                viewModel.dispatchEvent(AddTodoFormEvent.DescriptionChanged(newDescription))
            },
            maxLines = 4,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input_todo_description")
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = formState.isDone,
                onCheckedChange = { checked ->
                    viewModel.dispatchEvent(AddTodoFormEvent.TodoStatusChanged(checked))
                },
                modifier = Modifier.testTag("input_todo_is_done")
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.is_done))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.dispatchEvent(AddTodoFormEvent.Submit)
            },
            modifier = Modifier
                .align(Alignment.End)
                .testTag("input_todo_submit")
        ) {
            Text(stringResource(R.string.add_todo))
        }
    }
}
