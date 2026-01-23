package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var tvMonthYearLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        tvMonthYearLabel = findViewById(R.id.tvMonthYearLabel)

        // 7 કોલમ વાળું ગ્રીડ સેટ કરો
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
    }

    fun setupCalendar(monthData: List<JsonObject>, selectedHeader: String, title: String) {
        tvMonthYearLabel.text = title
        
        val daysList = monthData.map { json ->
            val date = json.get("ENGLISH")?.asString ?: ""
            val dayData = CalendarDayData(date, json)
            
            // કલર કોડિંગ લોજિક
            val category = json.get("Category")?.asString ?: ""
            dayData.colorCode = when {
                category.contains("Holiday") || category.contains("Sunday") -> 1 // Red
                category.contains("Hindu") -> 2 // Orange
                category.contains("Muslim") -> 3 // Green
                category.contains("Christian") -> 4 // Blue
                category.contains("Personal") -> 5 // Pink
                else -> 0
            }
            dayData
        }

        calendarRecyclerView.adapter = CalendarAdapter(daysList, selectedHeader)
    }
}
