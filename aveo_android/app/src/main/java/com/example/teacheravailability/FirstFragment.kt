package com.example.teacheravailability

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.teacheravailability.models.Teacher
import com.example.teacheravailability.services.ServiceBuilder
import com.example.teacheravailability.services.TeacherService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private fun loadTeachers() {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getTeacherList()

        requestCall.enqueue(object: Callback<List<Teacher>> {
            override fun onResponse(
                call: Call<List<Teacher>>?,
                response: Response<List<Teacher>>?
            ) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val teacherList = response.body()!!
                    } else { // application level failure
                        Toast.makeText(context, "Failed to retrieve items!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Teacher>>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred"+t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadTeacherByID(id: Int) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getTeacherByID(id)

        requestCall.enqueue(object: Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>?, response: Response<Teacher>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val teacherByID = response.body()!!
                    } else { // application level failure
                        Toast.makeText(context, "Failed to retrieve teacher by ID!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onFailure(call: Call<Teacher>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred"+t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonGo).setOnClickListener {
            val teacherID = view.findViewById<EditText>(R.id.teacherIdInput).text.toString()
            loadTeacherByID(teacherID.toInt())
            if (teacherID != "") {
                val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(teacherID)
                findNavController().navigate(action)
            }
            else {
                Toast.makeText(context, getString(R.string.teacher_id_blank_message), Toast.LENGTH_SHORT).show()
            }
        }
    }
}