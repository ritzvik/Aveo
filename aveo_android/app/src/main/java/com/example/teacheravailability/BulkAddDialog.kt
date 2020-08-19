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

class BulkAddDialog : DialogFragment() {

    private var validSlots: List<ValidSlot> = listOf()
    private val smallDays = makeSmallDays()
    private val triggerValidSlotView = MutableLiveData<Boolean>(false)
    private var placeholderValidSlots = listOf<ValidSlot>()
    private var alreadyAvailableSlots = listOf<AvailableSlot>()
    private var alreadyAvailableSlotsFetched = false

    companion object {

        private const val KEY_TID = "KET_TID"

        fun newInstance(tID: Int): BulkAddDialog {
            val args = Bundle()
            args.putInt(KEY_TID, tID)
            val fragment = BulkAddDialog()
            fragment.arguments = args
            return fragment
        }
    }

    private fun displayTeacherName(id: Int, textView: TextView) {
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
                        Toast.makeText(
                            context,
                            "Failed to retrieve teacher by ID!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Teacher>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAllAvailableSlotsByTeacherID(tID: Int) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getAllAvailableSlotsByTeacherID(tID)

        requestCall.enqueue(object : Callback<List<AvailableSlot>> {
            override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                Toast.makeText(
                    context,
                    "Available Slots by Teacher ID couldn't be fetched | " + t.toString(),
                    Toast.LENGTH_SHORT
                ).show()
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
                        Toast.makeText(
                            context,
                            "Available Slots by Teacher ID couldn't be fetched! ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

        })
    }

    private fun fetchAllValidSlots() {
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
                        Toast.makeText(
                            context,
                            "Failed to retrieve valid slots!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<ValidSlot>>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayValidSlots() {
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


            val validSlotViewAdaptor = ValidSlotViewAdaptor(placeholderValidSlots)
            validSlotViewAdaptor!!.initializeAdaptor()
            slotSelectorRecyclerView.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = validSlotViewAdaptor
            }
        }
    }

    private fun addNewAvailableSlots(tID: Int, newAvailableSlots: List<AvailableSlot>) {
        // Toast.makeText(context, newAvailableSlots.toString(), Toast.LENGTH_SHORT).show()

        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val postRequest = teacherService.setAvailability(newAvailableSlots)

        postRequest.enqueue(object : Callback<List<AvailableSlot>> {
            override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<List<AvailableSlot>>?,
                response: Response<List<AvailableSlot>>?
            ) {
                // Add Some code, currently crashing
            }

        })

    }

    private fun dateObjToDateString(date: Date): String {
        var dateString = (date.year + 1900).toString() + "-"
        dateString += (date.month + 1).toString().padStart(2, '0') + "-"
        dateString += date.date.toString()
        // Toast.makeText(context, dateString + " | " + date.day, Toast.LENGTH_SHORT).show()
        return dateString
    }

    private fun dayOfWeek(date: Date): Int {
        // 0 for Monday, 1 for Tuesday..... 6 for Sunday
        return if (date.day == 0) {
            6
        } else {
            (date.day - 1)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun executeSelections(startDateString: String, endDateString: String) {
        val tID = arguments?.getInt(KEY_TID)!!
        val newAvailableSlots = mutableListOf<AvailableSlot>()
        var startDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20")!!
        var endDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20")!!

        try {
            startDate = SimpleDateFormat("yyyy-MM-dd").parse(startDateString)!!
            endDate = SimpleDateFormat("yyyy-MM-dd").parse((endDateString))!!
            if (endDate.compareTo(startDate) <= 0) {
                throw Exception("Start Date more than End Date")
            }
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Make Sure you have entered the dates and Start Date is less than End Date!",
                Toast.LENGTH_SHORT
            ).show()
        }

        val dateIter = DateIterator(startDate, endDate, 1)
        while (dateIter.hasNext()) {
            val d = dateIter.next()
            val dString = dateObjToDateString(d)

            if (smallDays[dayOfWeek(d)].selected.value!!) {
                placeholderValidSlots.forEach { pvs ->
                    if (pvs.selectedValidSlotViewHolder) {
                        val start_time = pvs.start_time
                        var validSlotObject = validSlots.find { s ->
                            (s.start_time == start_time && s.day == dayOfWeek(d))
                        }
                        var validSlotID = validSlotObject!!.id
                        val newAvailableSlot = AvailableSlot(null, dString, 1, tID, validSlotID)
                        newAvailableSlots.add(newAvailableSlot)
                    }
                }
            }
        }
        addNewAvailableSlots(tID, newAvailableSlots.toList())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater!!.inflate(R.layout.fragment_bulk_add, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tID = arguments?.getInt(KEY_TID)!!
        displayTeacherName(tID, view.findViewById<TextView>(R.id.dialogTextView))
        fetchAllValidSlots()
        fetchAllAvailableSlotsByTeacherID(tID)

        val daySelectAdaptor = DaySelectAdaptor(smallDays!!)
        daySelectAdaptor!!.initializeAdaptor()
        daySelectorRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = daySelectAdaptor
        }

        smallDays.forEach { d ->
            d.selected.observe(viewLifecycleOwner, Observer { newSelectedStatus ->
                displayValidSlots()
            })
        }
        triggerValidSlotView.observe(viewLifecycleOwner, Observer { t ->
            displayValidSlots()
        })

        val startDateButton = view.findViewById<Button>(R.id.startDateButton)
        val endDateButton = view.findViewById<Button>(R.id.endDateButton)
        val startDateView = view.findViewById<TextView>(R.id.startDate)
        val endDateView = view.findViewById<TextView>(R.id.endDate)
        val doneButton = view.findViewById<Button>(R.id.buttonBulkAdd)

        startDateButton.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    startDateView.text = "" + year + "-" + (monthOfYear + 1).toString()
                        .padStart(2, '0') + "-" + dayOfMonth

                },
                year,
                month,
                day
            )

            dpd.show()
        }

        endDateButton.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    endDateView.text = "" + year + "-" + (monthOfYear + 1).toString()
                        .padStart(2, '0') + "-" + dayOfMonth

                },
                year,
                month,
                day
            )

            dpd.show()
        }

        doneButton.setOnClickListener { v ->
            val startDateString = startDateView.text!!.toString()
            val endDateString = endDateView.text!!.toString()

            if (validSlots.isNotEmpty() && alreadyAvailableSlotsFetched) {
                executeSelections(startDateString, endDateString)
            }

            val fm = view.findFragment<BulkAddDialog>()
            fm.dismiss()
        }


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