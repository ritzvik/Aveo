package com.example.teacheravailability.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.R

data class SmallDay (
    var position: Int = 0,
    var text: String = "Z",
    var selected: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)
)

fun makeSmallDays(): List<SmallDay> {
    var mutableSmallDayList: MutableList<SmallDay> = mutableListOf()
    val dayTexts: List<String> = listOf("M","T","W","T","F","S","S")
    val dayPosition: List<Int> = listOf(0,1,2,3,4,5,6)

    dayTexts.zip(dayPosition).forEach{ it ->
        mutableSmallDayList.add(SmallDay(it.second, it.first, MutableLiveData(true)))
    }

    return mutableSmallDayList.toList()
}

class DaySelectAdaptor(var smallDays: List<SmallDay>): RecyclerView.Adapter<DaySelectHolder>() {

    fun initializeAdaptor() {  }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaySelectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.slot_item, parent, false)
        return DaySelectHolder(view)
    }

    override fun getItemCount(): Int {
        return 7
    }

    override fun onBindViewHolder(holder: DaySelectHolder, position: Int) {
        holder.bind(smallDays[position])
    }

    fun getSelectedStatus(): List<Boolean> {
        val mutableList = mutableListOf<Boolean>()
        smallDays.forEach { d ->
            mutableList.add(d.selected.value!!)
        }
        return mutableList.toList()
    }

    fun getSelectedStatus(day: Int): Boolean {
        return smallDays[day].selected.value!!
    }
}
class DaySelectHolder(viewItem: View): RecyclerView.ViewHolder(viewItem) {
    private val dayBox = viewItem.findViewById<CheckBox>(R.id.checkBoxSlot)

    fun bind(smallDay: SmallDay){
        dayBox?.text = smallDay.text
        dayBox?.isChecked = smallDay.selected.value!!
        dayBox?.setOnCheckedChangeListener {compoundButton: CompoundButton?, b: Boolean ->
            smallDay.selected.value = b
        }
    }

}
