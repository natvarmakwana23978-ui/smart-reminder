package com.indian.calendar

import com.google.gson.annotations.SerializedName

data class CalendarItem(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = ""
)
