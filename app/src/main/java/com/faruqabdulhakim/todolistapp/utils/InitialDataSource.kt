package com.faruqabdulhakim.todolistapp.utils

import com.faruqabdulhakim.todolistapp.data.model.Todo

object InitialDataSource {
    fun getTodoList(): List<Todo> {
        return listOf(
            Todo(
                title = "Basic Kotlin",
                description = "Self-paced learning on Dicoding Class \"Memulai Pemrograman Dengan Kotlin\"",
                isDone = true,
            ),
            Todo(
                title = "Beginner Android",
                description = "Self-paced learning on Dicoding Class \"Belajar Membuat Aplikasi Android untuk Pemula\"",
                isDone = true,
            ),
            Todo(
                title = "UX Design",
                description = "Self-paced learning on Dicoding Class \"Belajar Dasar UX Design\"",
                isDone = true,
            ),
            Todo(
                title = "Fundamental Android",
                description = "Self-paced learning on Dicoding Class \"Belajar Fundamental Aplikasi Android\"",
                isDone = true,
            ),
            Todo(
                title = "Intermediate Android",
                description = "Self-paced learning on Dicoding Class \"Belajar Pengembangan Aplikasi Android Intermediate\"",
                isDone = true,
            ),
            Todo(
                title = "SOLID",
                description = "Self-paced learning on Dicoding Class \"Belajar Prinsip Pemrograman SOLID\"",
                isDone = true,
            ),
            Todo(
                title = "Android Compose",
                description = "Self-paced learning on Dicoding Class \"Belajar Membuat Aplikasi Android dengan Jetpack Compose\"",
                isDone = false,
            ),
            Todo(
                title = "Clean Architecture",
                description = "Learn clean architecture in Android Development",
                isDone = false,
            ),
            Todo(
                title = "Library: Koin",
                description = "Learn dependency injection using Koin",
                isDone = false,
            ),
            Todo(
                title = "AAD Certification prepare",
                description = "Prepare for AAD Certification",
                isDone = false,
            ),
        )
    }
}