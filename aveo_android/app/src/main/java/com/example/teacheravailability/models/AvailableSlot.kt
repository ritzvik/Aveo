package com.example.teacheravailability.models

data class AvailableSlot(
    var id: Int = 0,
    var teacher_id: Int = 0,
    var validslot_id: Int = 0,
    var date: String = "",
    var status: Int = 1
)
