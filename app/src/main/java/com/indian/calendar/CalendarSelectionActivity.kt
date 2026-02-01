package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        val spinnerCalendar = findViewById<Spinner>(R.id.calendarSpinner)
        val spinnerLanguage = findViewById<Spinner>(R.id.languageSpinner)
        val btnNext = findViewById<Button>(R.id.btnOpenCalendar)

        // ૧. કેલેન્ડર લિસ્ટ
        val calendars = arrayOf("વિક્રમ સંવત (ગુજરાતી)", "Hijri (Islamic)", "Gregorian (English)", "Saka Samvat", "+ નવું બનાવો")
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        spinnerCalendar.adapter = calAdapter

        // ૨. ઓટોમેટિક ભાષા બદલવાનું લોજિક
        spinnerCalendar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCal = calendars[position]
                val languages = when (selectedCal) {
                    "વિક્રમ સંવત (ગુજરાતી)" -> arrayOf("ગુજરાતી", "हिन्दी", "English")
                    "Hijri (Islamic)" -> arrayOf("العربية", "اردو", "हिन्दी", "English")
                    "Gregorian (English)" -> arrayOf("English", "Español", "Français")
                    else -> arrayOf("English")
                }
                val langAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, languages)
                spinnerLanguage.adapter = langAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ૩. બટન દબાવતા ડેટા લાવવો
        btnNext.setOnClickListener {
            btnNext.isEnabled = false
            btnNext.text = "માહિતી આવી રહી છે..."

            val apiService = RetrofitClient.instance.create(ApiService::class.java)
            
            // અહીં મેં બાય-ડિફોલ્ટ સેટિંગ્સ કરી દીધા છે
            apiService.getCalendarData("Sheet1", "readAll").enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@MainActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", response.body().toString())
                        intent.putExtra("SELECTED_CAL", spinnerCalendar.selectedItem.toString())
                        intent.putExtra("SELECTED_LANG", spinnerLanguage.selectedItem.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "શીટ સાથે કનેક્શન નથી થઈ રહ્યું", Toast.LENGTH_LONG).show()
                    }
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "ઇન્ટરનેટ ચેક કરો માલિક!", Toast.LENGTH_LONG).show()
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }
            })
        }
    }
}
