package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var tvMonthYear: TextView
    private lateinit var calendarViewPager: ViewPager2
    private var selectedColIndex: Int = 0
    private var allSheetData: Map<String, CalendarDayData> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        tvMonthYear = findViewById(R.id.tvMonthYear)
        calendarViewPager = findViewById(R.id.calendarViewPager)

        // અગાઉની સ્ક્રીન પરથી પસંદ કરેલ ઇન્ડેક્સ મેળવો
        selectedColIndex = intent.getIntExtra("COLUMN_INDEX", 0)

        fetchDataFromSheet()
    }

    private fun fetchDataFromSheet() {
        RetrofitClient.instance.getCalendarData(selectedColIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    allSheetData = response.body()?.associateBy { it.date } ?: emptyMap()
                    setupCalendar()
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                Toast.makeText(this@CalendarViewActivity, "ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupCalendar() {
        val startCalendar = Calendar.getInstance()
        calendarViewPager.adapter = MonthPagerAdapter(this, startCalendar, allSheetData)
        calendarViewPager.setCurrentItem(500, false)

        calendarViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, position - 500)
                val sdf = SimpleDateFormat("MMMM yyyy", Locale("gu"))
                tvMonthYear.text = sdf.format(calendar.time)
            }
        })
    }
}
