package com.example.daydream.todo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.daydream.database.Todo
import com.example.daydream.database.TodoDatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoViewModel(
    val database: TodoDatabaseDao,
    application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val todos = database.getAllTodos()

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackbarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun addTodo(newTodo: Todo) {
        uiScope.launch {
            insert(newTodo)
        }
    }

    private suspend fun insert(todo: Todo) {
        withContext(Dispatchers.IO) {
            database.insert(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        uiScope.launch {
            delete(todo)
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun delete(todo: Todo) {
        withContext(Dispatchers.IO) {
            database.delete(todo)
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }
}
