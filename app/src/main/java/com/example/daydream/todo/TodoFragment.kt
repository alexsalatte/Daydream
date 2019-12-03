package com.example.daydream.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.daydream.R
import com.example.daydream.database.TodoDatabase
import com.example.daydream.databinding.FragmentTodoBinding
import com.google.android.material.snackbar.Snackbar

class TodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTodoBinding>(inflater,
            R.layout.fragment_todo, container, false)

        // Set up dividers for the list items
        val recyclerView = binding.todoList
        val itemDecor = DividerItemDecoration(this.activity!!.application, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)

        // get the view model and database dao
        val application = requireNotNull(this.activity).application
        val dataSource = TodoDatabase.getInstance(application).todoDatabaseDao
        val viewModelFactory = TodoViewModelFactory(dataSource, application)
        todoViewModel =
            ViewModelProvider(this, viewModelFactory).get(TodoViewModel::class.java)

        binding.lifecycleOwner = this
        binding.todoViewModel = todoViewModel

        // adapter for the recyclerview
        val adapter = TodoAdapter(TodoListener { todoTitle ->
            Toast.makeText(context, "$todoTitle was touched!", Toast.LENGTH_SHORT).show()
        })

        // swipe callback to delete items
        val itemTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val todo = adapter.currentList[viewHolder.adapterPosition]
                todoViewModel.deleteTodo(todo)
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.todoList)
        binding.todoList.adapter = adapter

        // observer so that list is updated along with the database
        todoViewModel.todos.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        // navigation
        binding.fab.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(TodoFragmentDirections.actionTodoFragmentToDetailsFragment())
        }

        // Snackbar shown when data is deleted
        todoViewModel.showSnackbarEvent.observe(viewLifecycleOwner, Observer {
            if (it) {
                Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT
                ).show()
                todoViewModel.doneShowingSnackbar()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        todoViewModel.onClear()
        return super.onOptionsItemSelected(item)
    }
}
