package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.teacheravailability.adaptors.SlotViewAdaptor
import com.example.teacheravailability.models.AvailableSlots
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import kotlinx.android.synthetic.main.fragment_third.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        requestCall.enqueue(object : Callback<List<AvailableSlots>>{
            override fun onResponse(call: Call<List<AvailableSlots>>?, response: Response<List<AvailableSlots>>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val slots = response.body()!!
                        slotsRecyclerView.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = SlotViewAdaptor(slots)
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
    }
}