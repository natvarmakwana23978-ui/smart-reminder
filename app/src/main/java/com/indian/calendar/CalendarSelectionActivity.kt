package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarListRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.getCalendars().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val calendars = response.body()!!
                    Log.d("CALENDAR_DATA", "Size: ${calendars.size}")
                    if (calendars.isEmpty()) {
                        Toast.makeText(this@CalendarSelectionActivity, "લિસ્ટ ખાલી છે", Toast.LENGTH_SHORT).show()
                    }
                    recyclerView.adapter = CalendarSelectionAdapter(calendars) { item ->
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("COL_INDEX", item.id?.toIntOrNull() ?: 1)
                        intent.putExtra("CALENDAR_NAME", item.name)
                        startActivity(intent)
                    }
                } else {
                    Log.e("API_ERROR", "Error Code: ${response.code()}")
                }
            }
            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("API_FAILURE", "Error: ${t.message}")
            }
        })
    }
}
