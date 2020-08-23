package com.example.teacheravailability

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacheravailability.adaptors.DaySelectAdaptor
import com.example.teacheravailability.adaptors.ValidSlotViewAdaptor
import com.example.teacheravailability.adaptors.makeSmallDays
import com.example.teacheravailability.models.AvailableSlot
import com.example.teacheravailability.models.Teacher
import com.example.teacheravailability.models.ValidSlot
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import kotlinx.android.synthetic.main.fragment_bulk_add.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class DateIterator(start: Date, endInclusive: Date, stepDays: Int) : Iterator<Date> {
    private var currentDate = start
    private var endDateInclusive = endInclusive
    private var skipDays = stepDays

    override fun hasNext(): Boolean {
        return (currentDate.compareTo(endDateInclusive) <= 0)
    }

    override fun next(): Date {
        var next = currentDate
        currentDate = Date(currentDate.time + skipDays * 24 * 60 * 60 * 1000)
        return next
    }

}

fun DialogFragment.shortToast(msg: String) {
    val t = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
    t.show()
}

open class BulkDialog : DialogFragment() {
    protected var oneDayInMilliseconds: Long = 24 * 60 * 60 * 1000
    protected var validSlots: List<ValidSlot> = listOf()
    protected val smallDays = makeSmallDays()
    protected val triggerValidSlotView = MutableLiveData<Boolean>(false)
    protected var placeholderValidSlots = listOf<ValidSlot>()
    protected var alreadyAvailableSlots = listOf<AvailableSlot>()
    protected var alreadyAvailableSlotsFetched = false


    protected fun displayTeacherName(id: Int, textView: TextView) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getTeacherByID(id)

        requestCall.enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>?, response: Response<Teacher>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val teacherByID = response.body()!!

                        val teacherFullName =
                            teacherByID.first_name.toString() + " " + teacherByID.last_name.toString()
                        textView.text = teacherFullName

                    } else { // application level failure
                        shortToast("Failed to retrieve teacher by ID!")
                    }
                }
            }

            override fun onFailure(call: Call<Teacher>?, t: Throwable?) {
                shortToast("Error Occurred " + t.toString())
            }
        })
    }

    protected fun fetchAllAvailableSlotsByTeacherID(tID: Int) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getAllAvailableSlotsByTeacherID(tID)

        requestCall.enqueue(object : Callback<List<AvailableSlot>> {
            override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                shortToast("Available Slots by Teacher ID couldn't be fetched | " + t.toString())
            }

            override fun onResponse(
                call: Call<List<AvailableSlot>>?,
                response: Response<List<AvailableSlot>>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        alreadyAvailableSlots = response.body()!!
                        alreadyAvailableSlotsFetched = true
                        triggerValidSlotView.value = !(triggerValidSlotView.value!!)
                    } else {
                        shortToast("Available Slots by Teacher ID couldn't be fetched! ")
                    }
                }
            }

        })
    }

    protected fun fetchAllValidSlots() {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getAllValidSlots()

        requestCall.enqueue(object : Callback<List<ValidSlot>> {
            override fun onResponse(
                call: Call<List<ValidSlot>>?,
                response: Response<List<ValidSlot>>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        validSlots = response.body()!!
                        triggerValidSlotView.value = !(triggerValidSlotView.value!!)

                    } else { // application level failure
                        shortToast("Failed to retrieve valid slots!")
                    }
                }
            }

            override fun onFailure(call: Call<List<ValidSlot>>?, t: Throwable?) {
                shortToast("Error Occurred" + t.toString())
            }
        })
    }

    protected fun displayValidSlots() {
        doAsync {
            if (validSlots.isNotEmpty()) {

                var timings = mutableListOf<String>()
                var timingsTemp = mutableListOf<String>()
                val placeholderValidSlotsMutable = mutableListOf<ValidSlot>()
                var smallDaysSelected = smallDays.count { it.selected.value!! }

                validSlots.forEach { s ->

                    if (smallDays[s.day].selected.value!!) {
                        timings.add(s.start_time!!)
                    }
                }
                timings.forEach { t ->
                    if (timings.count { it == t } == smallDaysSelected) {
                        timingsTemp.add(t)
                    }
                }
                timings = timingsTemp.toSet().toMutableList()

                timings.forEach { t ->
                    placeholderValidSlotsMutable.add(ValidSlot(-1, -1, t))
                }
                placeholderValidSlots = placeholderValidSlotsMutable.toList()

                uiThread {
                    val validSlotViewAdaptor = ValidSlotViewAdaptor(placeholderValidSlots)
                    validSlotViewAdaptor.initializeAdaptor()
                    slotSelectorRecyclerView.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = validSlotViewAdaptor
                    }
                }
            }
        }
    }

    protected fun dateObjToDateString(date: Date): String {
        var dateString = (date.year + 1900).toString() + "-"
        dateString += (date.month + 1).toString().padStart(2, '0') + "-"
        dateString += date.date.toString().padStart(2, '0')
        return dateString
    }

    protected fun dayOfWeek(date: Date): Int {
        // 0 for Monday, 1 for Tuesday..... 6 for Sunday
        return if (date.day == 0) {
            6
        } else {
            (date.day - 1)
        }
    }

    protected fun addNewAvailableSlots(tID: Int, newAvailableSlots: List<AvailableSlot>) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val postRequest = teacherService.setAvailability(newAvailableSlots)

        postRequest.enqueue(object : Callback<List<AvailableSlot>> {
            override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                shortToast("Error Occurred" + t.toString())
            }

            override fun onResponse(
                call: Call<List<AvailableSlot>>?,
                response: Response<List<AvailableSlot>>?
            ) {
                // Add Some code, currently crashing
            }

        })

    }

    @SuppressLint("SetTextI18n")
    protected fun setUpDatePicker(
        button: Button,
        dateView: TextView?,
        minDate: Long? = null,
        maxDate: Long? = null
    ) {
        button.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    dateView?.text = "" + year + "-" + (monthOfYear + 1).toString()
                        .padStart(2, '0') + "-" + dayOfMonth

                },
                year,
                month,
                day
            )
            minDate?.let {
                dpd.datePicker.minDate = minDate
            }
            maxDate?.let {
                dpd.datePicker.maxDate = maxDate
            }
            dpd.show()
        }
    }

    protected fun setObserversToRefreshValidSlots() {
        smallDays.forEach { d ->
            d.selected.observe(viewLifecycleOwner, Observer { newSelectedStatus ->
                displayValidSlots()
            })
        }

        triggerValidSlotView.observe(viewLifecycleOwner, Observer { t ->
            displayValidSlots()
        })
    }

    protected fun setUpDatePickers(minDate: Long?, maxDate: Long?) {
        val startDateButton = view?.findViewById<Button>(R.id.startDateButton)
        val endDateButton = view?.findViewById<Button>(R.id.endDateButton)
        val startDateView = view?.findViewById<TextView>(R.id.startDate)
        val endDateView = view?.findViewById<TextView>(R.id.endDate)
        val doneButton = view?.findViewById<Button>(R.id.buttonBulkAdd)

        setUpDatePicker(startDateButton!!, startDateView, minDate, maxDate)
        setUpDatePicker(endDateButton!!, endDateView, minDate, maxDate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}
