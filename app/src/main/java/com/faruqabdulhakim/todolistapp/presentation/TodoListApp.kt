package com.faruqabdulhakim.todolistapp.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.faruqabdulhakim.todolistapp.ui.navigation.NavRoute
import com.faruqabdulhakim.todolistapp.ui.navigation.Screen
import com.faruqabdulhakim.todolistapp.presentation.screen.about.AboutScreen
import com.faruqabdulhakim.todolistapp.presentation.screen.add.AddScreen
import com.faruqabdulhakim.todolistapp.presentation.screen.detail.DetailScreen
import com.faruqabdulhakim.todolistapp.presentation.screen.home.HomeScreen
import com.faruqabdulhakim.todolistapp.ui.theme.TodoListAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (listOf(
                    Screen.Home.route,
                    Screen.Add.route,
                    Screen.About.route
            ).any { it == currentRoute }) BottomNavigation(navController = navController)
        },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onNavigateToDetail = { todoId ->
                        navController.navigate(Screen.Detail.createRoute(todoId))
                    }
                )
            }
            composable(Screen.Add.route) {
                AddScreen(
                    onAddTodoSuccess = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Screen.About.route) {
                AboutScreen()
            }
            composable(
                Screen.Detail.route,
                listOf(
                    navArgument("id") {
                        type = NavType.IntType
                    }
                )
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: -1
                DetailScreen(
                    todoId = id,
                    onTodoDeleted = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TodoListAppTheme {
        TodoListApp()
    }
}

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val navRoutes = listOf(
        NavRoute(
            title = "Home",
            contentDescription = "home_page",
            icon = Icons.Default.Home,
            screen = Screen.Home
        ),
        NavRoute(
            title = "Add",
            contentDescription = "add_page",
            icon = Icons.Default.AddCircle,
            screen = Screen.Add
        ),
        NavRoute(
            title = "About",
            contentDescription = "about_page",
            icon = Icons.Default.Info,
            screen = Screen.About
        ),
    )

    NavigationBar(
        modifier = modifier.clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
    ) {
        navRoutes.forEach { navRoute ->
            NavigationBarItem(
                selected = currentRoute == navRoute.screen.route,
                label = { Text(text = navRoute.title) },
                alwaysShowLabel = false,
                icon = {
                    Icon(
                        imageVector = navRoute.icon,
                        contentDescription = navRoute.title
                    )
                },
                onClick = {
                    navController.navigate(navRoute.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
                    .semantics(mergeDescendants = true) {
                        contentDescription = navRoute.contentDescription ?: navRoute.title
                    }
            )
        }
    }
}
