package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML માં જે ID હોય તે જ અહીં વાપરવા પડે
        // જો ID 'calendarTitleText' કે 'calendarTitle' ન હોય તો આ એરર આપે છે
        val titleTxt = findViewById<TextView>(R.id.titleText) 
            ?: findViewById<TextView>(R.id.calendarTitle)
            ?: findViewById<TextView>(R.id.calendarTitleText)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
            ?: findViewById<RecyclerView>(R.id.calendarRecyclerView)

        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "Calendar"
        
        titleTxt?.text = calendarName

        recyclerView?.layoutManager = GridLayoutManager(this, 7)
        progressBar?.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    // સફળતા! અહીં ડેટા આવી ગયો છે.
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
