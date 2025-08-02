package com.giftcard8.todoapp.ui.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.giftcard8.todoapp.databinding.FragmentEditTaskBinding
import com.giftcard8.todoapp.db.task.TaskEntity
import com.giftcard8.todoapp.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTaskFragment : Fragment() {
    private lateinit var binding: FragmentEditTaskBinding
    private val newArgs: EditTaskFragmentArgs by navArgs()

    private lateinit var args: TaskEntity
    private val viewModel: TaskViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditTaskBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        args = newArgs.TASK
        binding.editTextTaskEdit.setText(args.title)

        binding.buttonUpdateTask.setOnClickListener {
            val newTitle = binding.editTextTaskEdit.text.toString().trim()
            if (newTitle.isNotEmpty()) {
                val updateTask = args.copy(title = newTitle)
                viewModel.updateTaskTitle(updateTask)
                findNavController().popBackStack()
            } else {
                Toast.makeText(requireContext(), "Title can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
