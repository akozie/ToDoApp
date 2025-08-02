package com.giftcard8.todoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.giftcard8.todoapp.databinding.TaskItemBinding
import com.giftcard8.todoapp.db.task.TaskEntity

class TaskAdapter(
    private val onToggle: (TaskEntity) -> Unit,
    private val onDelete: (TaskEntity) -> Unit,
    private val onEdit: (TaskEntity) -> Unit,
) : ListAdapter<TaskEntity, TaskAdapter.TaskViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): TaskViewHolder {
        val binding =
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(task: TaskEntity) {
            binding.checkBoxTask.setOnCheckedChangeListener(null)
            binding.textViewTaskTitle.text = task.title
            binding.checkBoxTask.isChecked = task.completed
            binding.textViewTaskTitle.paint.isStrikeThruText = task.completed

            binding.checkBoxTask.setOnCheckedChangeListener { _, _ ->
                onToggle(task)
            }

            binding.buttonDeleteTask.setOnClickListener {
                onDelete(task)
            }

            binding.root.setOnClickListener {
                onEdit(task)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<TaskEntity>() {
        override fun areItemsTheSame(
            oldItem: TaskEntity,
            newItem: TaskEntity,
        ) = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: TaskEntity,
            newItem: TaskEntity,
        ) = oldItem == newItem
    }
}
