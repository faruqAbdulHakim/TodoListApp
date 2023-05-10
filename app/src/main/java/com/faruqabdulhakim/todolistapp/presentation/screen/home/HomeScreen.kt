package com.faruqabdulhakim.todolistapp.presentation.screen.home

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.faruqabdulhakim.todolistapp.R
import com.faruqabdulhakim.todolistapp.domain.model.Todo
import com.faruqabdulhakim.todolistapp.ui.theme.TodoListAppTheme
import com.faruqabdulhakim.todolistapp.utils.InitialDataSource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToDetail: (todoId: Int) -> Unit = {},
) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val groupedTodoList by viewModel.todoList.collectAsState()
    val query by viewModel.query

    LaunchedEffect(key1 = LocalContext.current) {
        viewModel.getAllTodo()
    }

    TodoList(
        searchQuery = query,
        onSearchQueryChange = viewModel::searchTodo,
        groupedTodoList = groupedTodoList,
        onTodoUpdate = viewModel::updateTodo,
        onNavigateToDetail = onNavigateToDetail,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TodoList(
    modifier: Modifier = Modifier,
    searchQuery: String = "",
    onSearchQueryChange: (text: String) -> Unit = {},
    groupedTodoList: Map<Boolean, List<Todo>>,
    onTodoUpdate: (todo: Todo) -> Unit = {},
    onNavigateToDetail: (todoId: Int) -> Unit = {}
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = modifier
            .fillMaxSize()
            .testTag("lazy_column")
    ) {
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.fillMaxWidth()
            )
        }
        groupedTodoList
            .toSortedMap()
            .forEach { (isDone, todoList) ->
                stickyHeader {
                    Text(
                        text = stringResource(
                            if (isDone) R.string.completed_todo
                            else R.string.todo_list
                        ),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 12.dp)
                    )
                }

                if (todoList.isEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.empty_todo_list),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        )
                    }
                } else {
                    items(todoList, key = { it.id }) { todo ->
                        TodoCard(
                            title = todo.title,
                            isDone = todo.isDone,
                            onTodoStatusChanged = { newStatus ->
                                onTodoUpdate(todo.copy(isDone = newStatus))
                            },
                            onClick = { onNavigateToDetail(todo.id) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .animateItemPlacement(
                                    tween(durationMillis = 200)
                                ),
                        )
                    }
                }
            }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoListPreview() {
    TodoListAppTheme {
        TodoList(
            groupedTodoList = InitialDataSource
                .getTodoList()
                .mapIndexed { index, item -> item.copy(id = index) }
                .groupBy { it.isDone },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onQueryChange: (text: String) -> Unit = {},
    query: String = "",
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = null
            )
        },
        placeholder = { Text(stringResource(R.string.search_todo)) },
        singleLine = true,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    TodoListAppTheme {
        SearchBar()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoCard(
    modifier: Modifier = Modifier,
    onTodoStatusChanged: (isDone: Boolean) -> Unit = {},
    onClick: () -> Unit = {},
    title: String,
    isDone: Boolean,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = title
            )
            Checkbox(
                checked = isDone,
                onCheckedChange = onTodoStatusChanged
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TodoCardPreview() {
    val todo = InitialDataSource.getTodoList()[0]
    TodoListAppTheme {
        TodoCard(
            title = todo.title,
            isDone = todo.isDone
        )
    }
}