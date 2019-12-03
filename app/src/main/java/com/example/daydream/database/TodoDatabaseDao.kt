package com.example.daydream.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDatabaseDao {
    @Insert
    fun insert(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Query("SELECT * FROM todo_table WHERE todoId = :key")
    fun get(key: Long): Todo?

    @Query("DELETE FROM todo_table")
    fun clear()

    @Query("SELECT * FROM todo_table ORDER BY todoId ASC")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE todoID = :key")
    fun getTodoWithId(key: Long): LiveData<Todo>
}
