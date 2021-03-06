package com.example.teacheravailability

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacheravailability.adaptors.DaySelectAdaptor
import com.example.teacheravailability.models.AvailableSlot
import kotlinx.android.synthetic.main.fragment_bulk.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class BulkAddDialog : BulkDialog() {

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

    @SuppressLint("SimpleDateFormat")
    private fun executeSelections(tID: Int, startDateString: String, endDateString: String) {

        val newAvailableSlots = mutableListOf<AvailableSlot>()
        var startDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20")!!
        var endDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2020-12-20")!!

        try {
            startDate = SimpleDateFormat("yyyy-MM-dd").parse(startDateString)!!
            endDate = SimpleDateFormat("yyyy-MM-dd").parse((endDateString))!!
            if (endDate.compareTo(startDate) < 0) {
                throw Exception("Start Date more than End Date")
            }
        } catch (e: Exception) {
            shortToast("Make Sure you have entered the dates and Start Date is less than End Date!")
            return
        }

        val dateIter = DateIterator(startDate, endDate, 1)
        while (dateIter.hasNext()) {
            val d = dateIter.next()
            val dString = super.dateObjToDateString(d)

            if (super.smallDays[super.dayOfWeek(d)].selected.value!!) {
                super.placeholderValidSlots.forEach { pvs ->
                    if (pvs.selectedValidSlotViewHolder) {
                        val start_time = pvs.start_time
                        var validSlotObject = super.validSlots.find { s ->
                            (s.start_time == start_time && s.day == super.dayOfWeek(d))
                        }
                        var validSlotID = validSlotObject!!.id
                        val newAvailableSlot = AvailableSlot(null, dString, 1, tID, validSlotID)
                        val alreadyAvailableSlot = super.alreadyAvailableSlots.find { aas ->
                            (aas.date == dString && aas.validslot_id == validSlotID)
                        }
                        if (alreadyAvailableSlot == null) {
                            newAvailableSlots.add(newAvailableSlot)
                        }
                    }
                }
            }
        }

        super.addNewAvailableSlotsAndClose(tID, newAvailableSlots.toList())

        if (newAvailableSlots.isNotEmpty()) {
            GlobalObjects.triggerMonthViewUpdate.value =
                !(GlobalObjects.triggerMonthViewUpdate.value!!)
        }

        return

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val tID = arguments?.getInt(KEY_TID)!!
        super.displayTeacherName(
            tID,
            view.findViewById<TextView>(R.id.dialogTextView),
            " | Bulk Add"
        )
        super.fetchAllValidSlots()
        super.fetchAllAvailableSlotsByTeacherID(tID)

        val daySelectAdaptor = DaySelectAdaptor(super.smallDays)
        daySelectAdaptor.initializeAdaptor()
        daySelectorRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = daySelectAdaptor
        }

        super.setObserversToRefreshValidSlots()

        val startDateView = view.findViewById<TextView>(R.id.startDate)
        val endDateView = view.findViewById<TextView>(R.id.endDate)
        val doneButton = view.findViewById<Button>(R.id.buttonBulkAdd)

        super.setUpDatePickers(
            System.currentTimeMillis() - 1000,
            System.currentTimeMillis() + (super.oneDayInMilliseconds * 30)
        )

        doneButton.setOnClickListener { v ->
            val startDateString = startDateView.text!!.toString()
            val endDateString = endDateView.text!!.toString()

            if (super.validSlots.isNotEmpty() && super.alreadyAvailableSlotsFetched) {
                executeSelections(tID, startDateString, endDateString)
            }

            // super.closeDialogBox()
        }


        super.onViewCreated(view, savedInstanceState)
    }
}