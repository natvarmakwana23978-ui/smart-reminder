package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val viewPager = findViewById<ViewPager2>(R.id.calendarViewPager)
        val tvMonthYear = findViewById<TextView>(R.id.tvMonthYear)

        val jsonData = intent.getStringExtra("DATA") ?: "[]"
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"
        
        val allData: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)

        if (allData.isNotEmpty()) {
            val adapter = CalendarPagerAdapter(this, allData, selectedLang)
            viewPager.adapter = adapter
            viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    val months = listOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
                    tvMonthYear.text = "${months[position]} - 2026"
                }
            })
        }
    }
}
