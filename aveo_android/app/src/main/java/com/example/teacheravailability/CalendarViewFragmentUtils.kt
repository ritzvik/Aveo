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


class EventDecorator(context: Activity?, dates: Collection<CalendarDay>) : DayViewDecorator {
    private val drawable: Drawable?
    private val datesToBeDecorated = dates

    init { // You can set background for Decorator via drawable here
        drawable = ContextCompat.getDrawable(context!!, R.drawable.checkbox_off_background)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return datesToBeDecorated.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(DotSpan("8.0".toFloat(), Color.RED))
    }

}
