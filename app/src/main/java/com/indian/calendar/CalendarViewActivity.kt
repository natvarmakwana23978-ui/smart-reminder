package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
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
        val tvMonthYear = findViewById<TextView>(R.id.tvMonthYear)
        
        val jsonData = intent.getStringExtra("DATA")
        val lang = intent.getStringExtra("SELECTED_LANG") ?: "ENGLISH"

        // હેડરમાં અંગ્રેજી મહિનો અને વર્ષ બતાવવા માટે
        // આપણે ડેટાના પહેલા જાન્યુઆરી મહિના પરથી સેટ કરીશું
        tvMonthYear.text = "January - 2026" 

        rv.layoutManager = GridLayoutManager(this, 7)
        val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
        
        val finalItems = mutableListOf<CalendarDayData>()
        dataList.forEach { 
            finalItems.add(CalendarDayData(it.get("ENGLISH")?.asString ?: "", it)) 
        }
        
        rv.adapter = CalendarAdapter(finalItems, lang)
    }
}
