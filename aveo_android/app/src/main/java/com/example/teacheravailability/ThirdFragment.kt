package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import org.w3c.dom.Text

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ThirdFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
    }
}