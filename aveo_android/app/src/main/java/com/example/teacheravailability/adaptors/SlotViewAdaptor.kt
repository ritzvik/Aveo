package com.example.teacheravailability.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.R
import com.example.teacheravailability.models.AvailableSlots

class SlotViewAdaptor(val slotsData: List<AvailableSlots>): RecyclerView.Adapter<SlotViewHolder>() {

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

}
class SlotViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    private val slotTime = viewItem.findViewById<CheckBox>(R.id.slotCheckBox)

    fun bind(slot: AvailableSlots){
        slotTime.text = slot.start_time
        if (slot.slot?.size!! > 0) {
            println(slot.slot?.size!!)
            slotTime.isChecked = true
        }
    }
}