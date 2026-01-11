package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var rvCalendar: RecyclerView
    private val daysList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        rvCalendar = findViewById(R.id.rvCalendar)

        setupCalendar()
    }

    private fun setupCalendar() {
        // અહીં 'day' અને 'position' નો પ્રકાર (String, Int) સ્પષ્ટ કર્યો છે
        val adapter = CalendarAdapter(daysList) { day: String, position: Int ->
            // જ્યારે કોઈ તારીખ પર ક્લિક થાય ત્યારનું લોજિક
            onDateSelected(day)
        }
        
        rvCalendar.layoutManager = GridLayoutManager(this, 7)
        rvCalendar.adapter = adapter
        
        generateDates()
    }

    private fun generateDates() {
        daysList.clear()
        // ઉદાહરણ તરીકે ૧ થી ૩૦ તારીખ ઉમેરીએ છીએ
        for (i in 1..30) {
            daysList.add(i.toString())
        }
        rvCalendar.adapter?.notifyDataSetChanged()
    }

    private fun onDateSelected(date: String) {
        // અહીં તારીખ પસંદગીનું લોજિક આવશે
    }
}
