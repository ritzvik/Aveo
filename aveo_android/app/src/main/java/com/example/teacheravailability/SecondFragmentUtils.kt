package com.example.teacheravailability

import android.R
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
// import androidx.ui.geometry.Radius
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan


class dayDecorator(context: Activity?, day: CalendarDay) : DayViewDecorator {
    private val drawable: Drawable?
    var myDay = day
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day == myDay
    }

    override fun decorate(view: DayViewFacade) {
        // view.setSelectionDrawable(drawable!!)
        view.addSpan(DotSpan("7.0".toFloat(), Color.RED))
    }

    init { // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.checkbox_off_background)
    }
}
