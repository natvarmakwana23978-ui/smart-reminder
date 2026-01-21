package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarSelectionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCalendarList()
    }

    private fun loadCalendarList() {
        RetrofitClient.api.getCalendarList().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    recyclerView.adapter = CalendarSelectionAdapter(list) { selectedItem ->
                        // પસંદ કરેલી ભાષા મુજબ નવું પેજ ખુલશે [cite: 2026-01-21]
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("COL_INDEX", selectedItem.id?.toInt() ?: 1)
                        startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "કનેક્શન એરર", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
