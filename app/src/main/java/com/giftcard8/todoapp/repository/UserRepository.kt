package com.giftcard8.todoapp.repository

import com.giftcard8.todoapp.db.user.UserDao
import com.giftcard8.todoapp.db.user.UserEntity
import javax.inject.Inject

class UserRepository
    @Inject
    constructor(
        private val userDao: UserDao,
    ) {
        suspend fun registerUser(
            username: String,
            password: String,
        ): Boolean {
            val existing = userDao.checkUserExists(username)
            return if (existing == null) {
                userDao.insertUser(UserEntity(username = username, password = password))
                true
            } else {
                false
            }
        }

        suspend fun loginUser(
            username: String,
            password: String,
        ): Boolean {
            return userDao.getUser(username, password) != null
        }

        suspend fun getUser(
            username: String,
            password: String,
        ): UserEntity? {
            return userDao.getUser(username, password)
        }

        suspend fun checkUserExists(username: String): Boolean {
            return userDao.checkUserExists(username) != null
        }
    }
