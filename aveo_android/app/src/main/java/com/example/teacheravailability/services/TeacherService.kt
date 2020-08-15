package com.example.teacheravailability.services


import com.example.teacheravailability.models.ValidSlots
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
    fun getAvailability(@Path("id") id: Int, @Path("date") date: String): Call<List<ValidSlots>>

    @Headers("Content-Type: application/json")
    @POST("ta2/api/availableslot/")
    fun setAvailability(@Body slotData: List<Slot>): Call<List<Slot>>

    @HTTP(method = "DELETE", path = "ta2/api/availableslot/tid/{id}/delete/", hasBody = true)
    fun delAvailability(@Body slotId: List<Int>, @Path("id") id: Int): Call<Void>
}