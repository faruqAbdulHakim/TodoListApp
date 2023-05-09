package com.faruqabdulhakim.todolistapp.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object Add: Screen("add")
    object About: Screen("about")
    object Detail: Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/${id}"
    }
}