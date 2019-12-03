package com.example.daydream.todo

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.daydream.database.Todo

@BindingAdapter("todoTitleFormatted")
fun TextView.setTodoTitleFormatted(item: Todo) {
    item?.let {
        text = item.todoTitle
    }
}

@BindingAdapter("todoDetailsFormatted")
fun TextView.setTodoDetailsFormatted(item: Todo) {
    item?.let {
        text = item.todoDetails
    }
}
