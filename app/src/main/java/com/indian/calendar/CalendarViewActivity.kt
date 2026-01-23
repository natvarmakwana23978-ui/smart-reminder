package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R

class CalendarViewActivity : AppCompatActivity() {

    // આ રીતે ડિક્લેર કરવાથી 'Unresolved reference' એરર જતી રહેશે
    private var myRecyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML માં ID @+id/recyclerView હોવું જોઈએ
        myRecyclerView = findViewById(R.id.recyclerView)
        myRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

    fun setupCalendar(monthData: List<JsonObject>, selectedHeader: String) {
        myRecyclerView?.adapter = MonthAdapter(monthData, selectedHeader)
    }
}
