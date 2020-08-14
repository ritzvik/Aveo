package com.example.teacheravailability.services

import com.example.teacheravailability.models.AvailableSlots
import com.example.teacheravailability.models.Teacher
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TeacherService {

    @GET("ta2/api/teacher")
    fun getTeacherList(): Call<List<Teacher>>

    @GET("ta2/api/teacher/{id}/")
    fun getTeacherByID(@Path("id") id: Int): Call<Teacher>

    @GET("/ta2/api/availableslot/tid/{id}/date/{date}/")
    fun getAvailability(@Path("id") id: Int, @Path("date") date: String): Call<List<AvailableSlots>>
}