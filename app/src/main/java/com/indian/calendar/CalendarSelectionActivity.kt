package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {

    // આ લિસ્ટમાં આપણે ગૂગલ શીટનો ડેટા સાચવીશું
    private var allCalendarData: List<JsonObject> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        // હવે આપણે ડાયરેક્ટ ડેટા ખેંચવાને બદલે પહેલા કેલેન્ડરનું નામ નક્કી કરવું પડશે
        // અથવા ડિફોલ્ટ રીતે "calendarfinaldata" લોડ કરવું પડશે
        fetchData("calendarfinaldata") 
    }

    private fun fetchData(sheetName: String) {
        // ૧. RetrofitClient.api નો સીધો ઉપયોગ કરો (મેં પહેલા આપેલા કોડ મુજબ)
        RetrofitClient.api.getCalendarData(sheetName).enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    allCalendarData = response.body()!!
                    setupLanguageList()
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા મળ્યો નથી", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLanguageList() {
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        
        // તમારી ગૂગલ શીટના હેડર મુજબ જ અહીં નામ લખવા
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "إسلامي (Islamic)", "ENGLISH")
        
        val adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            // ૨. ડેટાને String માં ફેરવો (Gson વાપરીને)
            val gson = Gson()
            val jsonString = gson.toJson(allCalendarData)
            
            // ૩. Intent દ્વારા બીજી Activity માં મોકલો
            val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLang)
            intent.putExtra("CALENDAR_DATA", jsonString)
            startActivity(intent)
        }
        
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}
