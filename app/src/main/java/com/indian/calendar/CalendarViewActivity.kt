package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvHeader: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // XML ના આઈડી મુજબ જોડાણ
        recyclerView = findViewById(R.id.calendarRecyclerView)
        tvHeader = findViewById(R.id.tvMonthYearLabel)
        progressBar = findViewById(R.id.progressBar)

        // નીચેથી ઉપર સ્ક્રોલ કરવા માટે LinearLayoutManager નો ઉપયોગ
        recyclerView.layoutManager = LinearLayoutManager(this)

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"

        if (!jsonData.isNullOrEmpty()) {
            val dataList: List<JsonObject> = Gson().fromJson(
                jsonData, object : TypeToken<List<JsonObject>>() {}.type
            )

            tvHeader.text = "કેલેન્ડર ૨૦૨૬"
            
            // એડેપ્ટર સેટ કરો (અહીં તમારું કલર કોડિંગ અને ૧ તારીખ ગુરુવારે સેટ કરવાનું લોજિક છે)
            recyclerView.adapter = CalendarAdapter(dataList, selectedLang)
        } else {
            Toast.makeText(this, "ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
        }
    }
}
