package com.indian.calendar

data class CalendarDayData(
    val date: String,
    val tithi: String? = null,
    val festival: String? = null,
    val note: String? = null
)
