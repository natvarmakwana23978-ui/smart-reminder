package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var monthYearText: TextView
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        monthYearText = findViewById(R.id.monthYearTV)
        viewPager = findViewById(R.id.calendarViewPager) // XML માં ID 'calendarViewPager' રાખવું

        val startCalendar = Calendar.getInstance()
        val adapter = MonthPagerAdapter(startCalendar)
        viewPager.adapter = adapter

        // જ્યારે યુઝર મહિનો બદલે ત્યારે ઉપરનું નામ (Month Name) બદલવા માટે
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateMonthText(position)
            }
        })

        // હાલના મહિના પર સેટ કરવું
        viewPager.setCurrentItem(500, false) // ૧૨૦૦ માંથી વચલો ભાગ (અંદાજે ૪૦-૫૦ વર્ષ આગળ-પાછળ)
    }

    private fun updateMonthText(position: Int) {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MONTH, position - 500) // Position મુજબ મહિનો ગણવો
        
        val sharedPref = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val lang = sharedPref.getString("selected_lang", "gu") ?: "gu"
        val locale = Locale(lang)

        val sdf = SimpleDateFormat("MMMM yyyy", locale)
        monthYearText.text = sdf.format(calendar.time)
    }
}
