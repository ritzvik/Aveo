package com.example.teacheravailability

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.lifecycle.Observer
import com.example.teacheravailability.models.AvailableSlot
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class CalendarViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the slot_item for this fragment
        return inflater.inflate(R.layout.fragment_calendar_view, container, false)
    }

    val args: CalendarViewFragmentArgs by navArgs()

    private fun slotsMarkedAvailable(
        id: Int,
        month: Int,
        year: Int,
        calendar: MaterialCalendarView
    ) {
        calendar.removeDecorators()
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getAvailableSlotsByMonth(id, month, year)

        requestCall.enqueue(object : Callback<List<AvailableSlot>> {
            override fun onResponse(
                call: Call<List<AvailableSlot>>?,
                response: Response<List<AvailableSlot>>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        doAsync {
                            val dateSet: MutableSet<Date> = mutableSetOf()
                            val responseBody = response.body()!!
                            responseBody.forEach { slot ->
                                val dateObj = SimpleDateFormat("yyyy-MM-dd").parse(slot.date)
                                dateSet.add(dateObj)
                            }
                            val calendarDatesSet = mutableSetOf<CalendarDay>()
                            dateSet.forEach { d ->
                                calendarDatesSet.add(CalendarDay.from(year, month, d.date))
                            }

                            uiThread {
                                calendar.addDecorators(EventDecorator(activity, calendarDatesSet))
                            }
                        }
                    } else { // application level failure
                        Toast.makeText(context, "Failed to retrieve items!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setUpMaterialCalendar(view: View, tID: Int) {
        val calendar = view.findViewById<MaterialCalendarView>(R.id.materialCalendar)
        val initialDate = calendar.currentDate
        slotsMarkedAvailable(tID, initialDate.month, initialDate.year, calendar)

        calendar.setOnDateChangedListener { calendar, date, selected ->

            val todaysDateObj = Calendar.getInstance().time

            if (date.year >= (todaysDateObj.year + 1900) && date.month >= (todaysDateObj.month + 1) && date.day >= todaysDateObj.date) {
                val year = date.year
                val month = date.month
                val dayOfMonth = date.day
                val displayString =
                    year.toString() + "-" + (month).toString() + "-" + dayOfMonth.toString()

                val action =
                    CalendarViewFragmentDirections.actionCalendarViewFragmentToModifyAvailabilityFragment(
                        displayString,
                        tID,
                        year,
                        month,
                        dayOfMonth
                    )
                findNavController().navigate(action)
            }
        }

        calendar.setOnMonthChangedListener { calender, date ->

            slotsMarkedAvailable(tID, date.month, date.year, calendar)
        }

        (activity as? MainActivity)?.mApp?.triggerMonthViewUpdate!!.observe(
            viewLifecycleOwner,
            Observer { b ->
                slotsMarkedAvailable(
                    tID,
                    calendar.currentDate.month,
                    calendar.currentDate.year,
                    calendar
                )
            })
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teacherName = args.teacherNameArg
        val tID = args.teacherIDArg
        (activity as? MainActivity)?.mApp?.setGlobalTeacherID(tID)
        view.findViewById<TextView>(R.id.teacherName).text = "Welcome " + teacherName + "!"

        setUpMaterialCalendar(view, tID)

    }
}