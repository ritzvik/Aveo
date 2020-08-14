package com.example.teacheravailability.models

data class AvailableSlots (
        var id: Int = 0,
        var slot: List<Slot>? = null,
        var day: String? = null,
        var start_time: String? = null
)

data class Slot(
    var id: Int = 0,
    var date: String? = null,
    var status: Int? = null,
    var teacher_id: Int? = null,
    var validslot_id: Int? = null
)