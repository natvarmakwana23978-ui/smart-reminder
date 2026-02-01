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

        // તમારી XML મુજબના સાચા ID અહીં સેટ કર્યા છે
        val btnOpenCalendar = findViewById<Button>(R.id.btnOpenCalendar) 
        val calendarSpinner = findViewById<Spinner>(R.id.calendarSpinner)
        val languageSpinner = findViewById<Spinner>(R.id.languageSpinner)

        btnOpenCalendar.setOnClickListener {
            val selectedLanguage = languageSpinner.selectedItem.toString()
            val selectedCalendarIndex = calendarSpinner.selectedItemPosition
            
            // ડેટા લોડ કરવાનું શરૂ કરો
            loadAllData(selectedLanguage, selectedCalendarIndex)
        }
    }

    private fun loadAllData(selectedLang: String, calIndex: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        // તમારી નવી લિંક મુજબ "getData" એક્શનનો ઉપયોગ
        apiService.getCalendarData("Sheet1", "getData").enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful && response.body() != null) {
                    val fullJsonData = Gson().toJson(response.body())
                    
                    val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                    intent.putExtra("DATA", fullJsonData)
                    intent.putExtra("SELECTED_LANG", selectedLang)
                    intent.putExtra("CAL_INDEX", calIndex)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "સર્વર ભૂલ!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
