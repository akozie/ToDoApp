package com.giftcard8.todoapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.giftcard8.todoapp.db.task.TaskDao
import com.giftcard8.todoapp.db.task.TaskEntity
import com.giftcard8.todoapp.db.user.UserDao
import com.giftcard8.todoapp.db.user.UserEntity

@Database(entities = [UserEntity::class, TaskEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun taskDao(): TaskDao
}
