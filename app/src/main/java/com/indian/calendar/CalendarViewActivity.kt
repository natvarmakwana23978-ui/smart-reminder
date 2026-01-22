package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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
        
        // મહિનાઓ ઉપર-નીચે બતાવવા માટે LinearLayoutManager [cite: 2026-01-21]
        recyclerView.layoutManager = LinearLayoutManager(this)

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        progressBar.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex = colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                    // ડેટાને મહિના મુજબ ગ્રુપ કરવા માટે [cite: 2026-01-21]
                    val allData = response.body()!!
                    val groupedData = allData.groupBy { it.englishDate?.split(" ")?.get(1) ?: "" }
                    
                    // મેઈન એડપ્ટર જે મહિનાઓના લિસ્ટ બતાવશે [cite: 2026-01-21]
                    recyclerView.adapter = MonthAdapter(groupedData)
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar.visibility = View.GONE
            }
        })
    }
}
