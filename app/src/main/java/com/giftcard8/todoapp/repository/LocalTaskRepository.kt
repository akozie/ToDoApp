package com.giftcard8.todoapp.repository

import com.giftcard8.todoapp.db.task.TaskDao
import com.giftcard8.todoapp.db.task.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalTaskRepository
    @Inject
    constructor(
        private val taskDao: TaskDao,
    ) {
        fun getTasksForUser(userId: Int): Flow<List<TaskEntity>> {
            return taskDao.getTasksForUser(userId)
        }

        suspend fun addTask(task: TaskEntity) {
            taskDao.insertTask(task)
        }

        suspend fun updateTask(task: TaskEntity) {
            taskDao.updateTask(task)
        }

        suspend fun deleteTask(task: TaskEntity) {
            taskDao.deleteTask(task)
        }
    }
