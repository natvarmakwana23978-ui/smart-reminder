package com.indian.calendar

import com.google.gson.JsonObject

data class CalendarDayData(
    val englishDate: String,
    val allData: JsonObject // તમારી ૨૭ કોલમનો તમામ ડેટા અહીં રહેશે
)
