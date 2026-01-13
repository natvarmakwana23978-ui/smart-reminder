package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var calendarViewPager: ViewPager2
    private var selectedColIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        calendarViewPager = findViewById(R.id.calendarViewPager)
        
        // Intent થી ઇન્ડેક્સ મેળવો
        selectedColIndex = intent.getIntExtra("COLUMN_INDEX", 0)

        setupCalendar()
    }

    private fun setupCalendar() {
        val startCalendar = Calendar.getInstance()
        // પાસ કરો: Context, Calendar, અને પસંદ કરેલ Column Index
        val adapter = MonthPagerAdapter(this, startCalendar, selectedColIndex)
        calendarViewPager.adapter = adapter
        calendarViewPager.setCurrentItem(500, false)

        calendarViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, position - 500)
                val sdf = SimpleDateFormat("MMMM yyyy", Locale("gu"))
                tvMonthYear.text = sdf.format(calendar.time)
            }
        })
    }
}
