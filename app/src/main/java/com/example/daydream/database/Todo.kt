package com.example.daydream.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    var todoId: Long = 0L,

    @ColumnInfo(name = "todo_title")
    var todoTitle: String = "I want food!",

    @ColumnInfo(name = "todo_details")
    var todoDetails: String = "The secret was friendship all along"
)
