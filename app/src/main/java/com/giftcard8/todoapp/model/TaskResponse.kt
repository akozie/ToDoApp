package com.giftcard8.todoapp.model

data class TaskResponse(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int,
)
