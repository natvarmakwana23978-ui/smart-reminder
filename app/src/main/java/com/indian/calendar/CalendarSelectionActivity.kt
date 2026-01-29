package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        apiService = RetrofitClient.instance.create(ApiService::class.java)
        val spinner = findViewById<Spinner>(R.id.languageSpinner)
        val btnOpen = findViewById<Button>(R.id.btnOpenCalendar)

        val languages = listOf("ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "ENGLISH")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)

        btnOpen.setOnClickListener {
            val selectedLang = spinner.selectedItem.toString()
            fetchAllData(selectedLang)
        }
    }

    private fun fetchAllData(selectedLang: String) {
        val call1 = apiService.getCalendarData("Sheet1")
        val call2 = apiService.getCalendarData("Sheet2")

        call1.enqueue(object : Callback<List<JsonObject>> {
            override fun onResponse(call: Call<List<JsonObject>>, response1: Response<List<JsonObject>>) {
                val data1 = Gson().toJson(response1.body())
                call2.enqueue(object : Callback<List<JsonObject>> {
                    override fun onResponse(call: Call<List<JsonObject>>, response2: Response<List<JsonObject>>) {
                        val data2 = Gson().toJson(response2.body())
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", data1)
                        intent.putExtra("WEEKDAYS_DATA", data2)
                        intent.putExtra("SELECTED_LANG", selectedLang)
                        startActivity(intent)
                    }
                    override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {}
                })
            }
            override fun onFailure(call: Call<List<JsonObject>>, t: Throwable) {}
        })
    }
}
