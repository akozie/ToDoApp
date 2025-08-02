package com.giftcard8.todoapp.ui.task.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.giftcard8.todoapp.adapter.TaskAdapter
import com.giftcard8.todoapp.databinding.FragmentTaskListBinding
import com.giftcard8.todoapp.ui.auth.activity.AuthActivity
import com.giftcard8.todoapp.utils.UserPreferences
import com.giftcard8.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var adapter: TaskAdapter
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = binding.progressBar

        adapter =
            TaskAdapter(
                onToggle = { task ->
                    viewModel.toggleTaskCompletion(task)
                },
                onDelete = { task ->
                    showConfirmationDialog(
                        message = "Are you sure you want to delete this task?",
                        onConfirm = { viewModel.deleteTask(task) },
                    )
                },
                onEdit = { task ->
                    if (task.completed) {
                        Toast.makeText(
                            requireContext(),
                            "Oops! You can't edit this task. It has been marked as completed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else {
                        showConfirmationDialog(
                            message = "Do you want to edit this task?",
                            onConfirm = {
                                val action =
                                    TaskListFragmentDirections.actionTaskListFragmentToEditTaskFragment(
                                        task,
                                    )
                                findNavController().navigate(action)
                            },
                        )
                    }
                },
            )

        binding.recyclerViewTasks.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTasks.adapter = adapter

        lifecycleScope.launch {
            progressBar.visibility = View.VISIBLE
            delay(200)
            viewModel.loadTasks()
            progressBar.visibility = View.GONE
        }

        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable?.toString() ?: ""
            lifecycleScope.launch {
                progressBar.visibility = View.VISIBLE
                binding.searchInputLayout.visibility = View.VISIBLE
                viewModel.filterTasks(query)
                binding.searchInputLayout.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.tasks.collectLatest { taskList ->
                adapter.submitList(taskList)
                progressBar.visibility = View.GONE

                val isSearchQueryNotEmpty = binding.searchEditText.text.toString().isNotEmpty()
                val hasTasks = taskList.isNotEmpty()

                when {
                    hasTasks -> {
                        binding.recyclerViewTasks.visibility = View.VISIBLE
                        binding.emptyStateLayout.visibility = View.GONE
                        binding.searchInputLayout.visibility = View.VISIBLE
                        binding.noResult.visibility = View.GONE
                    }
                    isSearchQueryNotEmpty -> {
                        binding.recyclerViewTasks.visibility = View.GONE
                        binding.emptyStateLayout.visibility = View.GONE
                        binding.noResult.visibility = View.VISIBLE
                        binding.searchInputLayout.visibility = View.VISIBLE
                    }
                    else -> {
                        binding.noResult.visibility = View.GONE
                        binding.recyclerViewTasks.visibility = View.GONE
                        binding.emptyStateLayout.visibility = View.VISIBLE
                        binding.searchInputLayout.visibility = View.GONE
                    }
                }
            }
        }

        binding.buttonAddTask.setOnClickListener {
            val action = TaskListFragmentDirections.actionTaskListFragmentToAddTaskFragment()
            findNavController().navigate(action)
        }

        binding.buttonLogout.setOnClickListener {
            showConfirmationDialog(
                title = "Logout",
                message = "Are you sure you want to logout?",
                onConfirm = {
                    lifecycleScope.launch {
                        progressBar.visibility = View.VISIBLE
                        viewModel.logout()
                        delay(100) // optional buffer for DataStore clear
                        progressBar.visibility = View.GONE
                        requireActivity().startActivity(Intent(requireContext(), AuthActivity::class.java))
                        requireActivity().finish()
                    }
                },
            )
        }
    }

    private fun showConfirmationDialog(
        title: String = "Confirm Action",
        message: String,
        onConfirm: () -> Unit,
    ) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
