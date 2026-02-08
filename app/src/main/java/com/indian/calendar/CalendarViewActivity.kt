package com.indian.calendar

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.indian.calendar.databinding.ActivityCalendarViewBinding // તમારી XML ફાઈલ મુજબ નામ
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarViewBinding
    private var selectedCalendar: String = ""
    private var selectedLanguage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // અગાઉની સ્ક્રીનમાંથી પસંદ કરેલ કેલેન્ડર અને ભાષા મેળવો
        selectedCalendar = intent.getStringExtra("SELECTED_CALENDAR") ?: "Vikram Samvat 2082"
        selectedLanguage = intent.getStringExtra("SELECTED_LANG") ?: "Gujarati"

        // કેલેન્ડર માટે ૭ કોલમનું ગ્રીડ સેટ કરો
        binding.recyclerViewCalendar.layoutManager = GridLayoutManager(this, 7)

        // ગૂગલ શીટમાંથી ડેટા લોડ કરો
        loadCalendarData()
    }

    private fun loadCalendarData() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        // તમારી ApiService મુજબ 'calendar' અને 'action' પેરામીટર મોકલો
        apiService.getCalendarData(selectedCalendar, "read").enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val rootObject = response.body()
                    val dataArray = rootObject?.getAsJsonArray("data")

                    if (dataArray != null) {
                        val dayList = mutableListOf<JsonObject>()
                        for (i in 0 until dataArray.size()) {
                            dayList.add(dataArray[i].asJsonObject)
                        }

                        // તમારા CalendarAdapter નો ઉપયોગ કરીને ડેટા સેટ કરો
                        val adapter = CalendarAdapter(dayList, selectedLanguage)
                        binding.recyclerViewCalendar.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@CalendarViewActivity, "ડેટા લોડ કરવામાં ભૂલ આવી!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
