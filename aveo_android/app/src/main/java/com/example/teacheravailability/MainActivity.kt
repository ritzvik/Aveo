package com.example.teacheravailability

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private fun setUpBulkAddFab(fabButton: FloatingActionButton?) {
        fabButton?.setOnClickListener { view ->
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

    private fun setUpBulkDelFab(fabButton: FloatingActionButton?) {
        fabButton?.setOnClickListener { view ->
            val tID = GlobalObjects.getGlobalTeacherID()
            if (tID >= 1) {
                val ft = supportFragmentManager
                val newFragment = BulkDelDialog.newInstance(tID)
                newFragment.show(ft, "name")
            } else {
                Snackbar.make(view, "Valid Teacher ID not found", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        setUpBulkAddFab(findViewById<FloatingActionButton>(R.id.fabBulkAdd))
        setUpBulkDelFab(findViewById<FloatingActionButton>(R.id.fabBulkDel))
    }

}
