package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.adaptors.SlotViewAdaptor
import com.example.teacheravailability.models.AvailableSlots
import com.example.teacheravailability.models.AvailableSlotsState
import com.example.teacheravailability.models.Slot
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import kotlinx.android.synthetic.main.fragment_third.*
import kotlinx.android.synthetic.main.slot_item.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the slot_item for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    val args: ThirdFragmentArgs by navArgs()
    // args.dateNday: String  # displays the top text like "2020-08-20 | Thursday"
    // args.teacherIDArg: Int
    // args.year: Int
    // args.month: Int  # months start from 0, example : 0 for January, 1 for Feb
    // args.dayOfMonth: Int # starts from 1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dateText = view.findViewById<TextView>(R.id.dateDisplay)
        dateText.text = args.dateNday

        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getAvailability(args.teacherIDArg, args.dateNday)
        var slotAdapter: SlotViewAdaptor? = null
        var slots: List<AvailableSlots>? = listOf()
        requestCall.enqueue(object : Callback<List<AvailableSlots>>{
            override fun onResponse(call: Call<List<AvailableSlots>>?, response: Response<List<AvailableSlots>>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        slots = response.body()!!
                        slotAdapter = SlotViewAdaptor(slots!!)
                        slotAdapter!!.intializeState()
                        slotsRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter =  slotAdapter
                        }

                    } else { // application level failure
                        Toast.makeText(
                            context,
                            "Failed to retrieve Slots!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<AvailableSlots>>?, t: Throwable?) {
                println(t.toString())
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        saveAvailabilityBtn.setOnClickListener{
            val currentState = slotAdapter?.getState()
            val state = parseAvailableSlots(slots)
            for (slot in state)  {
                println(currentState?.get(slot.key))
                println(slot)
            }
        }

    }
    fun parseAvailableSlots(slotsData: List<AvailableSlots>?): MutableMap<Int, AvailableSlotsState> {
        var state: MutableMap<Int, AvailableSlotsState> = mutableMapOf()
        if (slotsData != null) {
            for (item in slotsData){
                var status = false
                if (item.slot?.size!! > 0) {
                    status = true
                }
                var item_state = AvailableSlotsState(
                    item.id,
                    status
                )
                state.put(item.id,item_state)
            }
        }
        return state
    }
}