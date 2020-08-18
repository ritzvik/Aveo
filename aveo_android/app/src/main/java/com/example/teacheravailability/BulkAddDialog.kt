package com.example.teacheravailability

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacheravailability.adaptors.DaySelectAdaptor
import com.example.teacheravailability.adaptors.ValidSlotViewAdaptor
import com.example.teacheravailability.adaptors.makeSmallDays
import com.example.teacheravailability.models.Teacher
import com.example.teacheravailability.models.ValidSlot
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import kotlinx.android.synthetic.main.fragment_bulk_add.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class BulkAddDialog : DialogFragment() {

    private var validSlots: List<ValidSlot> = listOf()
    private val smallDays = makeSmallDays()
    private val triggerValidSlotView = MutableLiveData<Boolean>(false)
    private var placeholderValidSlots = listOf<ValidSlot>()

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
                if (timings.count{ it == t} == smallDaysSelected){
                    timingsTemp.add(t)
                }
            }
            timings = timingsTemp.toSet().toMutableList()

            timings.forEach { t ->
                placeholderValidSlotsMutable.add(ValidSlot(-1,-1,t))
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
        val enddateButton = view.findViewById<Button>(R.id.endDateButton)
        val startDateView = view.findViewById<EditText>(R.id.startDate)
        val endDateView = view.findViewById<EditText>(R.id.endDate)

        startDateButton.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                startDateView.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)

            }, year, month, day)

            dpd.show()
        }

        endDateButton.setOnClickListener { v ->
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                endDateView.setText("" + dayOfMonth + "/" + (monthOfYear+1) + "/" + year)

            }, year, month, day)

            dpd.show()
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