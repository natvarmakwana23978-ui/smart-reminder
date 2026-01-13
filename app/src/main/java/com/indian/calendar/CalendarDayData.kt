package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarDayData(
    @SerializedName("date") val date: String,      // શીટની તારીખ
    @SerializedName("detail") val detail: String,  // પસંદ કરેલ કેલેન્ડરની વિગત (તિથિ)
    @SerializedName("festival") val festival: String // તહેવારની વિગત
)
