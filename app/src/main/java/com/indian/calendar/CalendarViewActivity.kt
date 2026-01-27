package com.indian.calendar

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

// ડેટા હોલ્ડર ક્લાસ (એરર ટાળવા માટે અહીં જ વ્યાખ્યાયિત કર્યો છે)
data class CalendarDayData(val englishDate: String, val allData: JsonObject)

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

        // નીચેથી ઉપર સ્ક્રોલ કરવા માટે (તમારી પસંદગી મુજબ)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val jsonData = intent.getStringExtra("DATA")
        val selectedLang = intent.getStringExtra("SELECTED_LANG") ?: "ગુજરાતી (Gujarati)"

        if (!jsonData.isNullOrEmpty()) {
            try {
                val dataList: List<JsonObject> = Gson().fromJson(
                    jsonData, object : TypeToken<List<JsonObject>>() {}.type
                )

                val daysList = mutableListOf<CalendarDayData?>()

                // પગલું ૫: ૧ જાન્યુઆરી ૨૦૨૬ એ ગુરુવાર છે, એટલે ૪ ખાલી ખાના ઉમેરો
                for (i in 1..4) {
                    daysList.add(null)
                }

                // ગૂગલ શીટના ડેટાને એડેપ્ટર મુજબ સેટ કરો
                dataList.forEach { json ->
                    val dateStr = json.get("ENGLISH")?.asString ?: ""
                    daysList.add(CalendarDayData(dateStr, json))
                }

                tvHeader.text = "કેલેન્ડર ૨૦૨૬"
                recyclerView.adapter = CalendarAdapter(daysList, selectedLang)

            } catch (e: Exception) {
                Toast.makeText(this, "ડેટા પ્રોસેસિંગમાં ભૂલ: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
        }
    }
}
