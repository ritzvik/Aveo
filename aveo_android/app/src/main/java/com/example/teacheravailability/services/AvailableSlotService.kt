package com.example.teacheravailability.services

import com.example.teacheravailability.models.AvailableSlot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AvailableSlotService {

    @GET("ta2/api/availableslot/tid/{tid}/m/{month}/y/{year}/")
    fun getAvailableSlotsByMonth(
        @Path("tid") tid: Int,
        @Path("month") month: Int,
        @Path("year") year: Int
    ): Call<List<AvailableSlot>>
}
