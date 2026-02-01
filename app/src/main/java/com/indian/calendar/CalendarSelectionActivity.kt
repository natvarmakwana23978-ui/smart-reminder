package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        // ૧. XML માંથી બટન અને સ્પિનર શોધો (ID તમારા XML મુજબ ચેક કરી લેવા)
        val btnNext = findViewById<Button>(R.id.btnNext) // 'આગળ વધો' બટન
        val spinnerLang = findViewById<Spinner>(R.id.spinnerLanguage) 
        val spinnerCal = findViewById<Spinner>(R.id.spinnerCalendar)

        // ૨. બટન પર ક્લિક થાય ત્યારે જ ડેટા લોડ થવો જોઈએ
        btnNext.setOnClickListener {
            val selectedLanguage = spinnerLang.selectedItem.toString()
            val selectedSheet = "Sheet1" // અથવા સ્પિનર મુજબ નક્કી કરો

            Toast.makeText(this, "લોડ થઈ રહ્યું છે...", Toast.LENGTH_SHORT).show()
            loadCalendarAndDays(selectedSheet, selectedLanguage)
        }
    }

    private fun loadCalendarAndDays(sheetName: String, selectedLang: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getCalendarData(sheetName, "getData").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val calendarData = Gson().toJson(response.body())
                    fetchDayNames(apiService, calendarData, selectedLang)
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા ના મળ્યો!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchDayNames(apiService: ApiService, calendarData: String, selectedLang: String) {
        apiService.getCalendarData("Sheet2", "getDays").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                val dayNames = listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
                val dayNamesJson = Gson().toJson(dayNames)

                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("DATA", calendarData)
                intent.putExtra("SELECTED_LANG", selectedLang)
                intent.putExtra("DAY_NAMES", dayNamesJson)
                startActivity(intent)
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("DATA", calendarData)
                intent.putExtra("SELECTED_LANG", selectedLang)
                startActivity(intent)
            }
        })
    }
}
