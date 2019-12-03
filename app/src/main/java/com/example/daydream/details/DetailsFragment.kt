package com.example.daydream.details

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.daydream.R
import com.example.daydream.database.Todo
import com.example.daydream.database.TodoDatabase
import com.example.daydream.databinding.FragmentDetailsBinding
import com.example.daydream.todo.TodoViewModel
import com.example.daydream.todo.TodoViewModelFactory

class DetailsFragment : Fragment() {
    private lateinit var binding: FragmentDetailsBinding
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_details, container, false)

        // connect with the database and todoviewmodel
        val application = requireNotNull(this.activity).application
        val dataSource = TodoDatabase.getInstance(application).todoDatabaseDao
        val todoViewModelFactory = TodoViewModelFactory(dataSource, application)
        todoViewModel =
            ViewModelProvider(this, todoViewModelFactory).get(TodoViewModel::class.java)

        binding.lifecycleOwner = this

        binding.buttonCancel.setOnClickListener { view: View ->
            hideKeyboardNavigate(view)
        }

        binding.buttonAdd.setOnClickListener { view: View ->
            addTodo(view)
        }

        return binding.root
    }

    private fun addTodo(view: View) {
        val newTodo = Todo()
        if (binding.editTitle.text.toString() != "") {
            newTodo.todoTitle = binding.editTitle.text.toString()
        }
        if (binding.editDetails.text.toString() != "") {
            newTodo.todoDetails = binding.editDetails.text.toString()
        }
        todoViewModel.addTodo(newTodo)

        hideKeyboardNavigate(view)
    }

    private fun hideKeyboardNavigate(view: View) {
        // Hide the keyboard.
        val inputMethodManager = activity!!
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

        view.findNavController()
            .navigate(DetailsFragmentDirections
                .actionDetailsFragmentToTodoFragment())
    }
}
