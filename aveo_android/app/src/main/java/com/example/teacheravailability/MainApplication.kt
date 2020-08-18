package com.example.teacheravailability

import android.app.Application

class TeacherAvailabilityApp : Application() {
    private var tID: Int = -1

    public fun getGlobalTeacherID(): Int {
        return tID
    }

    public fun setGlobalTeacherID(id: Int) {
        if (id==-1 || id>=1) {
            tID = id
        }
    }
}
