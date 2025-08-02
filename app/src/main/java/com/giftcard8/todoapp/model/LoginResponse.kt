package com.giftcard8.todoapp.model

data class LoginResponse(
    val token: String,
    val id: Int,
    val username: String,
    val email: String,
)
