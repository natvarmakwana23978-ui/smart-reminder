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

        // ૧. વૈશ્વિક કેલેન્ડર લિસ્ટ - જે તમે કીધું તે મુજબ
        val calendars = arrayOf("વિક્રમ સંવત (ગુજરાતી)", "Hijri (Islamic)", "Chinese Calendar", "Saka Samvat", "Gregorian (English)", "+ નવું બનાવો")
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        spinnerCalendar.adapter = calAdapter

        // ૨. કેલેન્ડર મુજબ ભાષાઓનું સેટિંગ
        spinnerCalendar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCal = calendars[position]
                val languages = when (selectedCal) {
                    "વિક્રમ સંવત (ગુજરાતી)" -> arrayOf("ગુજરાતી", "हिन्दी", "English")
                    "Hijri (Islamic)" -> arrayOf("العربية", "اردو", "ગુજરાતી", "हिन्दी", "English")
                    "Chinese Calendar" -> arrayOf("Mandarin (中文)", "English")
                    else -> arrayOf("English", "ગુજરાતી", "हिन्दी")
                }
                val langAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_dropdown_item, languages)
                spinnerLanguage.adapter = langAdapter
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // ૩. ડેટા લાવવા માટેનું બટન
        btnNext.setOnClickListener {
            btnNext.isEnabled = false
            btnNext.text = "ડેટા લોડ થઈ રહ્યો છે..."

            val apiService = RetrofitClient.instance.create(ApiService::class.java)
            
            // તમારી શીટ મુજબ અહીં "Sheet2" કરી દીધું છે
            apiService.getCalendarData("Sheet2", "readAll").enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && response.body() != null) {
                        val intent = Intent(this@MainActivity, CalendarViewActivity::class.java)
                        intent.putExtra("DATA", response.body().toString())
                        intent.putExtra("SELECTED_CAL", spinnerCalendar.selectedItem.toString())
                        intent.putExtra("SELECTED_LANG", spinnerLanguage.selectedItem.toString())
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@MainActivity, "કનેક્શન નિષ્ફળ! શીટનું નામ ચેક કરો.", Toast.LENGTH_LONG).show()
                    }
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_LONG).show()
                    btnNext.isEnabled = true
                    btnNext.text = "આગળ વધો"
                }
            })
        }
    }
}
