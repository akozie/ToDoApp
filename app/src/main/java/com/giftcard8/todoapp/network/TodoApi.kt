package com.giftcard8.todoapp.network

import com.giftcard8.todoapp.model.*
import retrofit2.Response
import retrofit2.http.*

interface TodoApi {
    @POST("auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET("todos/user/1") // example user
    suspend fun getTasks(): Response<TodoListResponse>

    @POST("todos/add")
    suspend fun addTask(
        @Body task: TaskRequest,
    ): Response<TaskResponse>

    @PUT("todos/{id}")
    suspend fun updateTaskStatus(
        @Path("id") taskId: Int,
        @Body update: Map<String, Boolean>,
    ): Response<TaskResponse>

    @DELETE("todos/{id}")
    suspend fun deleteTask(
        @Path("id") taskId: Int,
    ): Response<Unit>

    @PUT("todos/{id}")
    suspend fun updateTaskTitle(
        @Path("id") id: Int,
        @Body update: Map<String, String>,
    ): Response<TaskResponse>
}
