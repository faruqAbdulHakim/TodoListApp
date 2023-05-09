package com.faruqabdulhakim.todolistapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faruqabdulhakim.todolistapp.data.model.Todo
import com.faruqabdulhakim.todolistapp.utils.InitialDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        var INSTANCE: TodoDatabase? = null

        @JvmStatic
        fun getInstance(context: Context, applicationScope: CoroutineScope): TodoDatabase {
            if (INSTANCE == null) {
                INSTANCE = synchronized(this) {
                    Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "Todo.db"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                INSTANCE?.let { todoDatabase ->
                                    applicationScope.launch {
                                        val todoDao = todoDatabase.todoDao()
                                        todoDao.insertList(InitialDataSource.getTodoList())
                                    }
                                }
                            }
                        })
                        .build()
                }
            }
            return INSTANCE as TodoDatabase
        }
    }
}