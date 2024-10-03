package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface Api {
    @GET("/get/list")
    fun getList(): Call<List<TaskItem>>

    @GET("/get/task/{id}")
    fun getTask(@Path("id") id: String): Call<TaskItem>

    @PUT("/load/list")
    fun loadList(@Body body: List<TaskItem>): Call<Boolean>

    @POST("/load/task")
    fun loadTask(@Body taskItem: TaskItem): Call<Boolean>

    @DELETE("/delete/{id}")
    fun deleteTask(@Path("id") id: String): Call<Boolean>

    @PATCH("/change/task/{id}")
    fun changeTask(@Path("id") id: String, @Body taskItem: TaskItem): Call<Boolean>

    @POST("/change/status/{id}")
    fun changeStatus(@Path("id") id: String): Call<Boolean>
}