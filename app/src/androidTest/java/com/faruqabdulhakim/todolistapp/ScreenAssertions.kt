package com.faruqabdulhakim.todolistapp

import androidx.navigation.NavController
import org.junit.Assert.assertEquals

fun NavController.assertCurrentRouteName(message: String, expectedRouteName: String) =
    assertEquals(message, expectedRouteName, currentBackStackEntry?.destination?.route)
