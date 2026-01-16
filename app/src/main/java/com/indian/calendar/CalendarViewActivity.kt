package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.model.CalendarDayData
import java.util.Calendar

data class CalendarData(val title: String)

class CalendarViewActivity : AppCompatActivity() {

    // Calendar date → CalendarDayData
    private val calendarMap: Map<String, CalendarDayData> = mapOf(
        "2026-1-15" to CalendarDayData(
            Date = "2026/01/15",
            Gujarati_Month = "પોષ",
            Tithi = "વદ-૫",
            Day = "ગુરૂવાર",
            Festival_English = "Pongal"
        ),
        "2026-1-16" to CalendarDayData(
            Date = "2026/01/16",
            Gujarati_Month = "પોષ",
            Tithi = "વદ-૬",
            Day = "શુક્રવાર",
            Festival_English = "Makar Sankranti"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val calendar = Calendar.getInstance()
        val todayDataList = getCalendarDataFor(calendar)

        todayDataList.forEach {
            println(it.title)
        }
    }

    private fun getCalendarDataFor(calendar: Calendar): List<CalendarData> {
        val key = "${calendar.get(Calendar.YEAR)}-" +
                "${calendar.get(Calendar.MONTH) + 1}-" +
                "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val dayData = calendarMap[key] ?: return emptyList()

        val list = mutableListOf<CalendarData>()

        if (dayData.Festival_English.isNotEmpty()) {
            list.add(CalendarData("Festival: ${dayData.Festival_English}"))
        }

        return list
    }
}
