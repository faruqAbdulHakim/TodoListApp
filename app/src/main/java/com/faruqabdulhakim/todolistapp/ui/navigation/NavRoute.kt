package com.faruqabdulhakim.todolistapp.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class NavRoute(
    val title: String,
    val contentDescription: String? = null,
    val icon: ImageVector,
    val screen: Screen,
)