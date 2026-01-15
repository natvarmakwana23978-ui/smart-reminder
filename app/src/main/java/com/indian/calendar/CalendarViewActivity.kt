package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

// Example data classes – તમારું વાસ્તવિક data structure અહીં હોવું જોઈએ
data class CalendarDayData(val info: String)
data class CalendarData(val title: String)

class CalendarViewActivity : AppCompatActivity() {

    // Example Map storing calendar data
    private val calendarMap: Map<String, CalendarDayData> = mapOf(
        "2026-01-15" to CalendarDayData("Special Day")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        // ✅ Calendar instance સહી રીતે મેળવવું
        val calendar = Calendar.getInstance()

        // Example usage of calendarMap with a function that returns List<CalendarData>
        val calendarDataList: List<CalendarData> = getCalendarDataFor(calendar)
        // હવે calendarDataList ઉપયોગ કરી શકો
    }

    // Function that converts Calendar → List<CalendarData>
    private fun getCalendarDataFor(calendar: Calendar): List<CalendarData> {
        val key = "${calendar.get(Calendar.YEAR)}-" +
                  "${calendar.get(Calendar.MONTH) + 1}-" +
                  "${calendar.get(Calendar.DAY_OF_MONTH)}"
        return calendarMap[key]?.let { listOf(CalendarData(it.info)) } ?: emptyList()
    }
}
