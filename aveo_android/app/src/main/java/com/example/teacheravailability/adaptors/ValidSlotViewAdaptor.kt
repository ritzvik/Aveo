package com.example.teacheravailability.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.R
import com.example.teacheravailability.models.ValidSlot

class ValidSlotViewAdaptor(var validSlots: List<ValidSlot>): RecyclerView.Adapter<ValidSlotViewHolder>() {

    fun initializeAdaptor() {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValidSlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_item, parent, false)
        return ValidSlotViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  validSlots.size
    }

    override fun onBindViewHolder(holder: ValidSlotViewHolder, position: Int) {
        holder.bind(validSlots[position])
    }

}

class ValidSlotViewHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    private val validSlotBox = viewItem.findViewById<CheckBox>(R.id.checkBox)

    fun bind(validSlot: ValidSlot) {
        validSlotBox.text = validSlot.start_time
        validSlotBox.isChecked = false
        validSlotBox?.setOnCheckedChangeListener { compoundButton, b ->
            validSlot.selectedValidSlotViewHolder = b
        }
    }
}
