package com.faruqabdulhakim.todolistapp

import android.app.Application
import android.content.res.Resources
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        res = this.resources
    }

    companion object {
        private lateinit var res: Resources

        fun getResources() = res
    }
}