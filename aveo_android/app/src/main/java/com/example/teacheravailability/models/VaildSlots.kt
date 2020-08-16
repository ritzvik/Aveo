package com.example.teacheravailability.models

data class ValidSlot (
    var id: Int = 0,
    var day: Int = 0,
    var start_time: String? = null
)

data class ValidSlotAugmented (
        var id: Int = 0,
        var slot: List<AvailableSlot>? = null,
        var day: Int = 0,
        var start_time: String? = null
)

data class AvailableSlot(
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