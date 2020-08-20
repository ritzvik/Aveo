package com.example.teacheravailability

import androidx.lifecycle.MutableLiveData
import java.util.*

object GlobalObjects {
    private var tID = -1
    public var triggerMonthViewUpdate = MutableLiveData<Boolean>(false)

    public fun getGlobalTeacherID(): Int {
        return tID
    }

    public fun setGlobalTeacherID(id: Int) {
        if (id == -1 || id >= 1) {
            tID = id
        }
    }

}
