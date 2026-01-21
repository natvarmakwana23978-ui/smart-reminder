package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val tvHeader = findViewById<TextView>(R.id.tvMonthYearLabel)

        recyclerView.layoutManager = GridLayoutManager(this, 7)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex = colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    val daysList = response.body()!!
                    
                    // હેડરમાં મહિનો અને વર્ષ સેટ કરો [cite: 2026-01-21]
                    val firstDay = daysList[0].englishDate
                    val parts = firstDay?.split(" ")
                    if (parts != null && parts.size >= 4) {
                        tvHeader.text = "${parts[1]} ${parts[3]}"
                    }

                    recyclerView.adapter = CalendarAdapter(daysList)
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}
