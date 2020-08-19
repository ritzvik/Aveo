package com.example.teacheravailability.services


import com.example.teacheravailability.models.ValidSlotAugmented
import com.example.teacheravailability.models.AvailableSlot
import com.example.teacheravailability.models.Teacher
import com.example.teacheravailability.models.ValidSlot
import retrofit2.Call
import retrofit2.http.*

interface TeacherService {

    @GET("ta2/api/teacher/{id}/")
    fun getTeacherByID(@Path("id") id: Int): Call<Teacher>

    @GET("/ta2/api/availableslot/tid/{id}/date/{date}/")
    fun getAvailability(@Path("id") id: Int, @Path("date") date: String): Call<List<ValidSlotAugmented>>

    @Headers("Content-Type: application/json")
    @POST("ta2/api/availableslot/")
    fun setAvailability(@Body slotData: List<AvailableSlot>): Call<List<AvailableSlot>>

    @HTTP(method = "DELETE", path = "ta2/api/availableslot/tid/{id}/delete/", hasBody = true)
    fun delAvailability(@Body slotId: List<Int>, @Path("id") id: Int): Call<Void>

    @GET("ta2/api/availableslot/tid/{id}/m/{month}/y/{year}/")
    fun getAvailableSlotsByMonth(@Path("id") id: Int, @Path("month") month: Int, @Path("year") year: Int): Call<List<AvailableSlot>>

    @GET("ta2/api/validslot/")
    fun getAllValidSlots(): Call<List<ValidSlot>>

    @GET("ta2/api/availableslot/tid/{id}/")
    fun getAllAvailableSlotsByTeacherID(@Path("id") id: Int): Call<List<AvailableSlot>>
}