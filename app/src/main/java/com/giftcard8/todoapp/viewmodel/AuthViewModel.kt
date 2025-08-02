package com.giftcard8.todoapp.viewmodel

import androidx.lifecycle.*
import com.giftcard8.todoapp.repository.UserRepository
import com.giftcard8.todoapp.utils.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject
    constructor(
        private val repository: UserRepository,
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        val loginResult = MutableLiveData<Boolean>()

        fun login(
            username: String,
            password: String,
        ) {
            viewModelScope.launch {
                val success = repository.loginUser(username, password)
                loginResult.value = success

                if (success) {
                    val user = repository.getUser(username, password)
                    user?.let {
                        userPreferences.saveUser(id = it.id, username = it.username, token = "") // token is optional
                    }
                    errorMessage.value = null
                } else {
                    errorMessage.value = "Invalid credentials"
                }
            }
        }

        val registrationResult = MutableLiveData<Boolean>()
        val errorMessage = MutableLiveData<String?>()

        fun register(
            username: String,
            password: String,
        ) {
            viewModelScope.launch {
                val exists = repository.checkUserExists(username)
                if (exists) {
                    registrationResult.value = false
                    errorMessage.value = "Username already taken"
                } else {
                    val success = repository.registerUser(username, password)
                    if (success) {
                        val user = repository.getUser(username, password)
                        user?.let {
                            userPreferences.saveUser(it.id, it.username, "")
                            registrationResult.value = true
                        } ?: run {
                            registrationResult.value = false
                            errorMessage.value = "Registration failed"
                        }
                    } else {
                        registrationResult.value = false
                        errorMessage.value = "Failed to register"
                    }
                }
            }
        }
    }
