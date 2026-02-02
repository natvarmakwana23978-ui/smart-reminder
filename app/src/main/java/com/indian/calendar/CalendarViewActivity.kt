package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CalendarViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val recyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        
        // Intent માંથી ડેટા મેળવો
        val jsonData = intent.getStringExtra("DATA") ?: ""
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી"

        // ૧૨ મહિનાનું લિસ્ટ બનાવવા માટે એડેપ્ટર સેટ કરો
        val adapter = CalendarScrollAdapter(jsonData, selectedLang)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}
