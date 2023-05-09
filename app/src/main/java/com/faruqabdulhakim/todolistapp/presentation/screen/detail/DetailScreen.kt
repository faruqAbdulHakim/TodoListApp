package com.faruqabdulhakim.todolistapp.presentation.screen.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.faruqabdulhakim.todolistapp.R
import com.faruqabdulhakim.todolistapp.presentation.ViewModelFactory
import com.faruqabdulhakim.todolistapp.ui.theme.TodoListAppTheme
import com.faruqabdulhakim.todolistapp.utils.InitialDataSource

@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = ViewModelFactory.getInstance(LocalContext.current)),
    onTodoDeleted: () -> Unit = {},
    todoId: Int = -1,
) {
    val todo by viewModel.todo.collectAsState()

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.getTodoDetail(todoId)
    }

    if (todo == null) {
        TodoNull()
    } else {
        TodoDetail(
            modifier = modifier,
            onTodoStatusChange = {
                viewModel.toggleTodoStatus(todo!!)
            },
            onTodoDelete = {
                viewModel.deleteTodo(todo!!)
                onTodoDeleted()
            },
            title = todo!!.title,
            description = todo!!.description,
            isDone = todo!!.isDone
        )
    }
}

@Composable
fun TodoDetail(
    modifier: Modifier = Modifier,
    onTodoStatusChange: () -> Unit = {},
    onTodoDelete: () -> Unit = {},
    title: String,
    description: String,
    isDone: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Column {
            if (isDone) {
                FilledTonalButton(
                    onClick = onTodoStatusChange,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.todo_unmark_done))
                    }
                }
            } else {
                OutlinedButton(
                    onClick = onTodoStatusChange,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.todo_mark_done))
                    }
                }
            }
            OutlinedButton(
                onClick = onTodoDelete,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.tertiary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.todo_delete))
                }
            }
        }
    }
}

@Composable
fun TodoNull(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(stringResource(R.string.cant_find_data))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val todo = InitialDataSource.getTodoList()[0]
    TodoListAppTheme {
        TodoDetail(
            title = todo.title,
            description = todo.description,
            isDone = todo.isDone
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DataNotFoundPreview() {
    TodoListAppTheme {
        TodoNull()
    }
}