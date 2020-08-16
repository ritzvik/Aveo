package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.teacheravailability.adaptors.SlotViewAdaptor
import com.example.teacheravailability.models.AvailableSlot
import com.example.teacheravailability.models.ValidSlotAugmented
import com.example.teacheravailability.models.ValidSlotsState
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import kotlinx.android.synthetic.main.fragment_third.*
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
        var slots: List<ValidSlotAugmented>? = listOf()
        var state: MutableMap<Int, ValidSlotsState> = mutableMapOf()
        requestCall.enqueue(object : Callback<List<ValidSlotAugmented>>{
            override fun onResponse(call: Call<List<ValidSlotAugmented>>?, response: Response<List<ValidSlotAugmented>>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        slots = response.body()!!
                        state = parseAvailableSlots(slots)
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

            override fun onFailure(call: Call<List<ValidSlotAugmented>>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
        saveAvailabilityBtn.setOnClickListener{
            val currentState = slotAdapter?.getState()
            val delSlotList: MutableList<Int> = ArrayList()
            val addSlotList: MutableList<AvailableSlot> = ArrayList()
            for (slot in state)  {
//                println(currentState!!.get(slot.key)?.status.toString() +" "+slot.value.status.toString() )
                if (currentState!!.get(slot.key)?.status != slot.value.status){
                    if (currentState.get(slot.key)?.status!!) {
                        var newSLot = AvailableSlot(
                            null,
                            args.dateNday,
                            1,
                            args.teacherIDArg,
                            slot.key
                        )
                        addSlotList.add(newSLot)
                    }
                    else if (currentState.get(slot.key)?.status  == false){
                        delSlotList.add(slot.value.available_slot_id!!)
                    }
                }
            }

            // Api service
            val availablityService = ServiceBuilder.buildService(TeacherService::class.java)

            //Post Api call
            val postRequest = availablityService.setAvailability(addSlotList)
            postRequest.enqueue(object : Callback<List<AvailableSlot>>{
                override fun onFailure(call: Call<List<AvailableSlot>>?, t: Throwable?) {
                    Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<AvailableSlot>>?, response: Response<List<AvailableSlot>>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            println("-------------------Added---------------")
                            println(response.body())
                            var createdSlots = response.body()
                            for (slot in createdSlots!!)  {
                                state.get(slot.validslot_id)?.status = true
                                state.get(slot.validslot_id)?.available_slot_id = slot.id
                            }
                        }
                    }
                }

            })

            //Delete Api Call
            val deleteRequest = availablityService.delAvailability(delSlotList, args.teacherIDArg)
            deleteRequest.enqueue(object : Callback<Void>{
                override fun onFailure(call: Call<Void>?, t: Throwable?) {
                    Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<Void>?,
                    response: Response<Void>?
                ) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            println("---------------Deleted---------------")
                            for (slot in currentState!!)  {
                                state.get(slot.key)?.status = slot.value.status
                                Toast.makeText(context, "Changes Saved.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

            })

        }

    }
    fun parseAvailableSlots(slotsData: List<ValidSlotAugmented>?): MutableMap<Int, ValidSlotsState> {
        var state: MutableMap<Int, ValidSlotsState> = mutableMapOf()
        if (slotsData != null) {
            for (item in slotsData){
                var status = false
                if (item.slot?.size!! > 0) {
                    status = true
                }
                var available_slot_id: Int? = null
                if (item.slot!!.size > 0) available_slot_id = item.slot?.get(0)?.id
                var item_state = ValidSlotsState(
                    item.id,
                    status,
                    available_slot_id = available_slot_id
                )
                state.put(item.id,item_state)
            }
        }
        return state
    }
}
