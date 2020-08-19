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
class LoginFragment : Fragment() {

    private fun loadTeacherByIDandNavigate(id: Int) {
        val teacherService = ServiceBuilder.buildService(TeacherService::class.java)
        val requestCall = teacherService.getTeacherByID(id)

        requestCall.enqueue(object : Callback<Teacher> {
            override fun onResponse(call: Call<Teacher>?, response: Response<Teacher>?) {
                if (response != null) {
                    if (response.isSuccessful) {
                        val teacherByID = response.body()!!

                        val teacherFullName =
                            teacherByID.first_name.toString() + " " + teacherByID.last_name.toString()
                        val action = LoginFragmentDirections.actionLoginFragmentToCalendarViewFragment(
                            teacherFullName, id
                        )
                        findNavController().navigate(action)

                    } else { // application level failure
                        Toast.makeText(
                            context,
                            "Failed to retrieve teacher by ID!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<Teacher>?, t: Throwable?) {
                Toast.makeText(context, "Error Occurred" + t.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.buttonGo).setOnClickListener {
            val teacherID = view.findViewById<EditText>(R.id.teacherIdInput).text.toString()

            if (teacherID != "") {
                loadTeacherByIDandNavigate(teacherID.toInt())
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.teacher_id_blank_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
