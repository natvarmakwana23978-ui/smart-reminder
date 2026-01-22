package com.indian.calendar

import org.json.JSONObject

data class CalendarDayData(
    val englishDate: String,  // કોલમ A
    val localDate: String?,   // કોલમ B (ગુજરાતી તિથિ)
    val allData: JSONObject   // આખા રો નો ડેટા (બધી ૨૭ કોલમ્સ)
)
