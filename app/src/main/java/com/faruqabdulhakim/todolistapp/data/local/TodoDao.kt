package com.faruqabdulhakim.todolistapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.faruqabdulhakim.todolistapp.data.model.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(todo: Todo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(todoList: List<Todo>)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("SELECT * FROM todo WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun getAllTodos(query: String): Flow<List<Todo>>

    @Query("SELECT * FROM todo WHERE id=:id")
    fun getTodoById(id: Int): Flow<List<Todo>>
}