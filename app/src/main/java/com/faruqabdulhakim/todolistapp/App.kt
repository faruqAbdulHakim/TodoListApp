package com.faruqabdulhakim.todolistapp

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        res = this.resources
    }

    companion object {
        private lateinit var res: Resources
        private val applicationScope = CoroutineScope(SupervisorJob())

        fun getResources() = res
        fun getApplicationScope() = applicationScope
    }
}