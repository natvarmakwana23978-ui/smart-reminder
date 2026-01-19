package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
// model ઈમ્પોર્ટ હટાવી દીધો છે કારણ કે બધી ફાઈલો હવે com.indian.calendar પેકેજમાં જ છે

class CalendarDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view) // જરૂર મુજબ લેઆઉટ ચેક કરી લેવું

        val dayData = intent.getParcelableExtra<CalendarDayData>("day_data")
        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        
        dayData?.let {
            titleTxt?.text = "${it.Date}\n${it.Gujarati}"
        }
    }
}
