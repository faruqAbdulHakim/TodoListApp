package com.faruqabdulhakim.todolistapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.faruqabdulhakim.todolistapp.data.local.TodoDao
import com.faruqabdulhakim.todolistapp.data.local.TodoDatabase
import com.faruqabdulhakim.todolistapp.data.repository.TodoRepositoryImpl
import com.faruqabdulhakim.todolistapp.domain.repository.TodoRepository
import com.faruqabdulhakim.todolistapp.domain.usecase.ValidateInput
import com.faruqabdulhakim.todolistapp.domain.usecase.ValidateTitleInput
import com.faruqabdulhakim.todolistapp.utils.InitialDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Named
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext application: Context, provider: Provider<TodoDao>): TodoDatabase =
        Room.databaseBuilder(
            application,
            TodoDatabase::class.java,
            "Todo.db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(SupervisorJob()).launch(Dispatchers.IO) {
                        val todoDao = provider.get()
                        todoDao.insertList(InitialDataSource.getTodoList())
                    }
                }
            })
            .build()


    @Provides
    @Singleton
    fun provideTodoDao(db: TodoDatabase) = db.todoDao()

    @Provides
    @Singleton
    fun provideTodoRepository(todoDao: TodoDao): TodoRepository = TodoRepositoryImpl(todoDao)

    @Provides
    @Singleton
    @Named("titleInput")
    fun provideValidateTitleInput(): ValidateInput = ValidateTitleInput()
}