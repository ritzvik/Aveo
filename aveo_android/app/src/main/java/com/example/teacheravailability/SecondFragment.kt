package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    val args: SecondFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val teacherName = args.teacherNameArg
        val tID = args.teacherIDArg
        view.findViewById<TextView>(R.id.teacherName).text = teacherName

        val calender = view.findViewById<CalendarView>(R.id.calendarView)
        calender.setOnDateChangeListener { calender, year, month, dayOfMonth ->
            val dateString =
                year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()

            val action = SecondFragmentDirections.actionSecondFragmentToThirdFragment(dateString)
            findNavController().navigate(action)
        }

    }
}