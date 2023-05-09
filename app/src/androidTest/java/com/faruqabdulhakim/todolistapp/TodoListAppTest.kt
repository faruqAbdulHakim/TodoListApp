package com.faruqabdulhakim.todolistapp

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.filters.LargeTest
import com.faruqabdulhakim.todolistapp.presentation.TodoListApp
import com.faruqabdulhakim.todolistapp.ui.navigation.Screen
import com.faruqabdulhakim.todolistapp.ui.theme.TodoListAppTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@LargeTest
class TodoListAppTest {
    private lateinit var navController: TestNavHostController

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setUp() {
        composeTestRule.setContent {
            TodoListAppTheme {
                navController = TestNavHostController(LocalContext.current)
                navController.navigatorProvider.addNavigator(ComposeNavigator())
                TodoListApp(navController = navController)
            }
        }
    }

    @Test
    fun e2e_positive_case() {
        // case: start destination is home screen
        navController.assertCurrentRouteName("Initial screen should be Home screen", Screen.Home.route)

        // case: navigate to few screens correctly
        composeTestRule.onNodeWithContentDescription("add_page").performClick()
        navController.assertCurrentRouteName("Should navigate to Add screen", Screen.Add.route)
        composeTestRule.onNodeWithContentDescription("about_page").performClick()
        navController.assertCurrentRouteName("Should navigate to About screen", Screen.About.route)
        composeTestRule.onNodeWithContentDescription("home_page").performClick()
        navController.assertCurrentRouteName("Should navigate to Home screen", Screen.Home.route)

        // case: should add new todo
        composeTestRule.onNodeWithContentDescription("add_page").performClick()
        composeTestRule.onNodeWithTag("input_todo_title").performTextInput("Test Add Todo")
        composeTestRule.onNodeWithTag("input_todo_description").performTextInput("This is description")
        composeTestRule.onNodeWithTag("input_todo_submit").performClick()
        Thread.sleep(1000)
        navController.assertCurrentRouteName("Should go back to home after add new todo", Screen.Home.route)

        composeTestRule.onNodeWithContentDescription("add_page").performClick()
        composeTestRule.onNodeWithTag("input_todo_title").performTextInput("Test Add Completed Todo")
        composeTestRule.onNodeWithTag("input_todo_description").performTextInput("This is description")
        composeTestRule.onNodeWithTag("input_todo_is_done").performClick()
        composeTestRule.onNodeWithTag("input_todo_submit").performClick()
        Thread.sleep(1000)
        navController.assertCurrentRouteName("Should go back to home after add new todo", Screen.Home.route)

        composeTestRule.onNodeWithTag("lazy_column").performScrollToNode(hasText("Test Add Todo"))
        composeTestRule.onAllNodesWithText("Test Add Todo")[0].assertExists()
        composeTestRule.onNodeWithTag("lazy_column").performScrollToNode(hasText("Test Add Completed Todo"))
        composeTestRule.onAllNodesWithText("Test Add Completed Todo")[0].assertExists()

        // case: should navigate to detail screen
        composeTestRule.onAllNodesWithText("Test Add Completed Todo")[0].performClick()
        navController.assertCurrentRouteName("Should navigate to detail screen", Screen.Detail.route)
        composeTestRule.onNodeWithText("Test Add Completed Todo").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is description").assertIsDisplayed()
    }

    @Test
    fun e2e_negative_case() {
        composeTestRule.onNodeWithContentDescription("add_page").performClick()
        composeTestRule.onNodeWithTag("input_todo_submit").performClick()
        composeTestRule.onNodeWithText(
            composeTestRule.activity.getString(R.string.input_error_blank)
        ).assertIsDisplayed()
    }
}