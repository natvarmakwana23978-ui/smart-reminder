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

class CalendarSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        // ધારો કે યુઝરે ગુજરાત કેલેન્ડર બટન પર ક્લિક કર્યું
        // આપણે ડેટા લોડ કરવાનું ફંક્શન કોલ કરીશું
        loadCalendarAndDays("Sheet1", "ગુજરાતી (Gujarati)")
    }

    private fun loadCalendarAndDays(sheetName: String, selectedLang: String) {
        // અહીં આપણે તમારા 'RetrofitClient' નો ઉપયોગ કર્યો છે
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        // એરર સોલ્વ કરવા માટે અહીં "getData" પેરામીટર ઉમેર્યો છે
        apiService.getCalendarData(sheetName, "getData").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    val calendarData = Gson().toJson(response.body())
                    
                    // ડેટા મળી જાય એટલે વારના નામ (Sheet2) લેવા જઈશું
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
        // Sheet2 માંથી વારના નામ લાવવા "getDays" એક્શન મોકલીશું
        apiService.getCalendarData("Sheet2", "getDays").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                // અહીં આપણે અત્યારે મેન્યુઅલ લિસ્ટ મોકલીએ છીએ, જે તમે પછી શીટ મુજબ બદલી શકો
                val dayNames = listOf("રવિ", "સોમ", "મંગળ", "બુધ", "ગુરુ", "શુક્ર", "શનિ")
                val dayNamesJson = Gson().toJson(dayNames)

                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("DATA", calendarData)
                intent.putExtra("SELECTED_LANG", selectedLang)
                intent.putExtra("DAY_NAMES", dayNamesJson)
                startActivity(intent)
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                // જો વાર ના મળે તો પણ કેલેન્ડર તો ખોલીશું જ
                val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                intent.putExtra("DATA", calendarData)
                intent.putExtra("SELECTED_LANG", selectedLang)
                startActivity(intent)
            }
        })
    }
}
