package com.example.teacheravailability

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            val tID = GlobalObjects.getGlobalTeacherID()
            if (tID >= 1) {
                val ft = supportFragmentManager
                val newFragment = BulkAddDialog.newInstance(tID)
                newFragment.show(ft, "name")
            } else {
                Snackbar.make(view, "Valid Teacher ID not found", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }
    }

}
