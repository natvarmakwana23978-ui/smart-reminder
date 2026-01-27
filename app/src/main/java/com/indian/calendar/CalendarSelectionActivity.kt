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
    private var allData: List<JsonObject> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)
        fetchData()
    }

    private fun fetchData() {
        RetrofitClient.api.getCalendarData("calendarfinaldata").enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful && response.body() != null) {
                    allData = response.body()!!
                    setupList()
                }
            }
            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupList() {
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        
        // શીટના હેડર્સમાંથી ભાષાઓનું લિસ્ટ બનાવો (ENGLISH સિવાયની બધી)
        val headers = allData[0].keySet().filter { it != "ENGLISH" }.toMutableList()
        headers.add("➕ નવું કેલેન્ડર બનાવો (પગલું ૩-૪)")

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CalendarSelectionAdapter(headers) { selected ->
            if (selected.contains("નવું")) {
                // અહીં નવું કેલેન્ડર બનાવવાનું ફોર્મ ખુલશે
            } else {
                val intent = Intent(this, CalendarViewActivity::class.java)
                intent.putExtra("SELECTED_LANG", selected)
                intent.putExtra("DATA", Gson().toJson(allData))
                startActivity(intent)
            }
        }
    }
}
