package com.giftcard8.todoapp.ui.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.giftcard8.todoapp.databinding.FragmentAddTaskBinding
import com.giftcard8.todoapp.utils.UserPreferences
import com.giftcard8.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddTaskFragment : Fragment() {
    private lateinit var binding: FragmentAddTaskBinding
    private val viewModel: TaskViewModel by viewModels()

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveTask.setOnClickListener {
            val title = binding.editTextTaskTitle.text.toString().trim()
            if (title.isNotEmpty()) {
                viewModel.addTask(title)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Title can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
