package com.giftcard8.todoapp.model

data class TaskRequest(
    val todo: String,
    val completed: Boolean,
    val userId: Int,
)
