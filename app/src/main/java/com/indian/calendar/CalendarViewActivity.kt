package com.indian.calendar

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val rv = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        val sideBar = findViewById<LinearLayout>(R.id.sidebarWeekdays)
        val tvTitle = findViewById<TextView>(R.id.tvCalendarTitle)
        
        tvTitle.text = intent.getStringExtra("CAL_TITLE") ?: "કેલેન્ડર"
        val jsonData = intent.getStringExtra("DATA")
        val lang = intent.getStringExtra("SELECTED_LANG") ?: "ENGLISH"

        // ડાબી ઉભી પટ્ટી (Sidebar) માં વાર ઉમેરવા
        val weekdays = listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
        sideBar.removeAllViews()
        weekdays.forEach { day ->
            val tv = TextView(this).apply {
                text = day
                height = 200 // આશરે એક ખાનાની ઊંચાઈ
                gravity = Gravity.CENTER
                textSize = 12sp
            }
            sideBar.addView(tv)
        }

        rv.layoutManager = GridLayoutManager(this, 7)
        val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
        
        val finalItems = mutableListOf<CalendarDayData>()
        dataList.forEach { finalItems.add(CalendarDayData(it.get("ENGLISH")?.asString ?: "", it)) }
        
        rv.adapter = CalendarAdapter(finalItems, lang)
    }
}
