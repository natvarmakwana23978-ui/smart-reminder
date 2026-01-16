package com.indian.calendar

import com.indian.calendar.model.CalendarDayData

class CalendarRepository {

    fun getDummyCalendar(): List<CalendarDayData> {
        return listOf(
            CalendarDayData(
                Date = "2026/01/15",
                Gujarati_Month = "પોષ",
                Tithi = "વદ-૫",
                Day = "ગુરૂવાર",
                Festival_English = "Pongal"
            )
        )
    }
}
