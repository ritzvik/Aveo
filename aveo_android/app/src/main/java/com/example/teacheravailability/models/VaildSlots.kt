package com.example.teacheravailability.models

data class ValidSlots (
        var id: Int = 0,
        var slot: List<Slot>? = null,
        var day: String? = null,
        var start_time: String? = null
)

data class Slot(
    var id: Int? = null,
    var date: String? = null,
    var status: Int? = null,
    var teacher_id: Int? = null,
    var validslot_id: Int? = null
)

data class ValidSlotsState (
    var id: Int? = null,
    var status: Boolean? = false,
    var available_slot_id: Int? = -1
)