package com.indian.calendar

import android.os.Bundle
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
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL // ઉપર-નીચે સ્ક્રોલ

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: ""

        if (!jsonData.isNullOrEmpty()) {
            val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
            
            // ૧૨ મહિના માટે એડેપ્ટર સેટ કરો
            // (અહીં મહિના મુજબ ડેટા ફિલ્ટર કરવાનું લોજિક આવશે)
        }
    }
}
