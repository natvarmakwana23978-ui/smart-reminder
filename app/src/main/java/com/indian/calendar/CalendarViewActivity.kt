package com.indian.calendar

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
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

        // XML ના આઈડી સાથે જોડાણ
        recyclerView = findViewById(R.id.calendarRecyclerView)
        tvHeader = findViewById(R.id.tvMonthYearLabel)
        progressBar = findViewById(R.id.progressBar)

        // ગ્રીડ લેઆઉટ સેટ કરો: એક લાઈનમાં ૭ દિવસ (રવિવાર થી શનિવાર)
        recyclerView.layoutManager = GridLayoutManager(this, 7)

        // અગાઉની સ્ક્રીનમાંથી ડેટા મેળવો
        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"

        if (!jsonData.isNullOrEmpty()) {
            try {
                val dataList: List<JsonObject> = Gson().fromJson(
                    jsonData, object : TypeToken<List<JsonObject>>() {}.type
                )

                val daysList = mutableListOf<CalendarDayData?>()

                // પગલું ૫: ૧ જાન્યુઆરી ૨૦૨૬ એ ગુરુવાર છે.
                // રવિવાર(0), સોમવાર(1), મંગળવાર(2), બુધવાર(3) -> ૪ ખાલી ખાના ઉમેરો
                for (i in 0 until 4) {
                    daysList.add(null)
                }

                // ગૂગલ શીટનો ડેટા એડેપ્ટરના ફોર્મેટમાં ઉમેરો
                dataList.forEach { json ->
                    val dateStr = json.get("ENGLISH")?.asString ?: ""
                    daysList.add(CalendarDayData(dateStr, json))
                }

                // હેડર સેટ કરો
                tvHeader.text = "કેલેન્ડર ૨૦૨૬"
                
                // એડેપ્ટર સેટ કરો (આમાં કલર કોડિંગ અને ડેટા દેખાશે)
                recyclerView.adapter = CalendarAdapter(daysList, selectedLang)

            } catch (e: Exception) {
                Toast.makeText(this, "ભૂલ: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
        }
    }
}
