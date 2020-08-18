package com.example.teacheravailability.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.R
import com.example.teacheravailability.models.ValidSlotAugmented
import com.example.teacheravailability.models.ValidSlotsState


private var state: MutableMap<Int, ValidSlotsState>? = null

class SlotViewAdaptor(var slotsData: List<ValidSlotAugmented>): RecyclerView.Adapter<SlotViewHolder>() {

    fun intializeState(){
        state = mutableMapOf()
        for (item in this.slotsData){
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
            state!!.put(item.id,item_state)
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

    fun getState(): MutableMap<Int, ValidSlotsState>? {
        return state
    }

}

class SlotViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    private val slotTime = viewItem.findViewById<CheckBox>(R.id.checkBox)

    fun bind(slot: ValidSlotAugmented){
        slotTime.text = slot.start_time
        slotTime.tag = slot.id
        if (slot.slot?.size!! > 0) {
            slotTime.isChecked = true
        }
        slotTime?.setOnCheckedChangeListener{buttonView, isChecked ->
            var id = slotTime.tag as Int
            var slotState = state?.get(id)
            slotState?.status = !slotState?.status!!
            state?.put(id, slotState)
        }
    }
}