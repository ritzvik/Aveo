package com.example.teacheravailability.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.R
import com.example.teacheravailability.models.AvailableSlots
import com.example.teacheravailability.models.AvailableSlotsState


private var state: MutableMap<Int, AvailableSlotsState> = mutableMapOf()

class SlotViewAdaptor(var slotsData: List<AvailableSlots>): RecyclerView.Adapter<SlotViewHolder>() {

    fun intializeState(){
        for (item in this.slotsData){
            var status = false
            if (item.slot?.size!! > 0) {
                status = true
            }
            var item_state = AvailableSlotsState(
                item.id,
                status
            )
            state?.put(item.id,item_state)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_item, parent, false)
        return SlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return slotsData.size
    }

    override fun onBindViewHolder(holder: SlotViewHolder, position: Int) {
        holder.bind(slotsData[position])
    }

    fun getState(): MutableMap<Int, AvailableSlotsState> {
        return state
    }

}

class SlotViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    private val slotTime = viewItem.findViewById<CheckBox>(R.id.slotCheckBox)

    fun bind(slot: AvailableSlots){
        slotTime.text = slot.start_time
        slotTime.tag = slot.id
        if (slot.slot?.size!! > 0) {
            slotTime.isChecked = true
        }
        slotTime?.setOnCheckedChangeListener{buttonView, isChecked ->
            var id = slotTime.tag as Int
            var sloteState = state.get(id)
            sloteState?.status = !sloteState?.status!!
            state.put(id, sloteState)
        }
    }
}