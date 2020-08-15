package com.example.teacheravailability.services

import com.example.teacheravailability.models.AvailableSlots
import com.example.teacheravailability.models.Slot
import com.example.teacheravailability.models.Teacher
import retrofit2.Call
import retrofit2.http.*

interface TeacherService {

    @GET("ta2/api/teacher")
    fun getTeacherList(): Call<List<Teacher>>

    @GET("ta2/api/teacher/{id}/")
    fun getTeacherByID(@Path("id") id: Int): Call<Teacher>

    @GET("/ta2/api/availableslot/tid/{id}/date/{date}/")
    fun getAvailability(@Path("id") id: Int, @Path("date") date: String): Call<List<AvailableSlots>>

    @Headers("Content-Type: application/json")
    @POST("ta2/api/availableslot/")
    fun setAvailability(@Body slotData: Slot, @Path("date") date: String)

    @Headers("Content-Type: application/json")
    @POST("ta2/api/availableslot/tid/{}/delete/")
    fun delAvailability(@Body slotData: Slot, @Path("date") date: String)
}