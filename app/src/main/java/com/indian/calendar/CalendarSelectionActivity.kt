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
    private var allCalendarData: List<JsonObject> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)
        fetchData("calendarfinaldata")
    }

    private fun fetchData(sheetName: String) {
        RetrofitClient.api.getCalendarData(sheetName).enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response: Response<List<JsonObject>>) {
                if (response.isSuccessful) {
                    allCalendarData = response.body() ?: emptyList()
                    setupLanguageList()
                }
            }
            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupLanguageList() {
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "إسلامي (Islamic)", "ENGLISH")
        
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CalendarSelectionAdapter(languages) { selectedLang ->
            val jsonString = Gson().toJson(allCalendarData)
            val intent = Intent(this, CalendarViewActivity::class.java)
            intent.putExtra("SELECTED_LANGUAGE", selectedLang)
            intent.putExtra("CALENDAR_DATA", jsonString)
            startActivity(intent)
        }
    }
}
