package com.giftcard8.todoapp.viewmodel

import androidx.lifecycle.*
import com.giftcard8.todoapp.db.task.TaskEntity
import com.giftcard8.todoapp.repository.LocalTaskRepository
import com.giftcard8.todoapp.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel
    @Inject
    constructor(
        private val repository: LocalTaskRepository,
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        private val _allTasks = MutableStateFlow<List<TaskEntity>>(emptyList())
        val tasks = MutableStateFlow<List<TaskEntity>>(emptyList())

        fun loadTasks() {
            viewModelScope.launch {
                val userId = userPreferences.getUserId().firstOrNull()
                if (userId != null) {
                    repository.getTasksForUser(userId)
                        .distinctUntilChanged()
                        .collectLatest { taskList ->
                            _allTasks.value = taskList
                            tasks.value = taskList
                        }
                }
            }
        }

        fun filterTasks(query: String) {
            tasks.value =
                if (query.isBlank()) {
                    _allTasks.value
                } else {
                    _allTasks.value.filter {
                        it.title.contains(query.trim(), ignoreCase = true)
                    }
                }
        }

        fun addTask(title: String) {
            viewModelScope.launch {
                val userId = userPreferences.getUserId().firstOrNull() ?: return@launch
                val task = TaskEntity(userId = userId, title = title)
                repository.addTask(task)
            }
        }

        fun toggleTaskCompletion(task: TaskEntity) {
            viewModelScope.launch {
                val updated = task.copy(completed = !task.completed)
                repository.updateTask(updated)
                loadTasks()
            }
        }

        fun deleteTask(task: TaskEntity) {
            viewModelScope.launch {
                repository.deleteTask(task)
            }
        }

//        fun updateTaskTitle(task: TaskEntity) {
//            viewModelScope.launch {
//                val updated = task.copy(title = task.title, id = task.id)
//                repository.updateTask(updated)
//                loadTasks()
//            }
//        }

        fun updateTaskTitle(task: TaskEntity) {
            viewModelScope.launch {
                repository.updateTask(task) // task already has updated title
                loadTasks()
            }
        }

        fun logout() {
            viewModelScope.launch {
                userPreferences.logout()
            }
        }
    }
