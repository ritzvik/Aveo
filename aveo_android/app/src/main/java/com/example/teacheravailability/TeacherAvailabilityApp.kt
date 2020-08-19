package com.example.teacheravailability

import android.app.Application
import androidx.lifecycle.MutableLiveData

class TeacherAvailabilityApp : Application() {
    public var triggerMonthViewUpdate = MutableLiveData<Boolean>(false)
    private var tID: Int = -1

    public fun getGlobalTeacherID(): Int {
        return tID
    }

    public fun setGlobalTeacherID(id: Int) {
        if (id == -1 || id >= 1) {
            tID = id
        }
    }
}
