package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://script.google.com/macros/s/તમારી_સ્ક્રિપ્ટ_આઈડી/") // તમારી સાચી URL અહીં મૂકજો
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // ઉદાહરણ તરીકે ગુજરાતી કેલેન્ડર પર ક્લિક થાય ત્યારે
        // લોજિક: પહેલા Sheet1 (ડેટા) અને પછી Sheet2 (વાર) ખેંચીશું
        loadCalendarAndDays("Sheet1", "ગુજરાતી (Gujarati)")
    }

    private fun loadCalendarAndDays(sheetName: String, selectedLang: String) {
        // ૧. કેલેન્ડર ડેટા (Sheet1) લોડ કરો
        // અહીં "getData" એ 'action' પેરામીટર તરીકે પાસ કર્યું છે જેથી એરર સોલ્વ થાય
        apiService.getCalendarData(sheetName, "getData").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful) {
                    val calendarData = Gson().toJson(response.body())
                    
                    // ૨. હવે વારના નામ (Sheet2) લોડ કરો
                    fetchDayNames(calendarData, selectedLang)
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchDayNames(calendarData: String, selectedLang: String) {
        // Sheet2 માંથી વારના નામ લાવવા માટે "getDays" એક્શન વાપરો
        apiService.getCalendarData("Sheet2", "getDays").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful) {
                    // અહીં તમારી શીટના લોજિક મુજબ પસંદ કરેલી ભાષાના વાર ફિલ્ટર કરો
                    // અત્યારે આપણે એક સેમ્પલ લિસ્ટ મોકલીએ છીએ
                    val dayNamesJson = Gson().toJson(listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ"))

                    val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                    intent.putExtra("DATA", calendarData)
                    intent.putExtra("SELECTED_LANG", selectedLang)
                    intent.putExtra("DAY_NAMES", dayNamesJson)
                    startActivity(intent)
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                // જો વાર લોડ ન થાય તો પણ કેલેન્ડર તો ખોલો જ
                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("DATA", calendarData)
                intent.putExtra("SELECTED_LANG", selectedLang)
                startActivity(intent)
            }
        })
    }
}
