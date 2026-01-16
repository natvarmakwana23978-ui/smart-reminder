package com.indian.calendar.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CalendarDayData(
    val Date: String,
    val Gujarati_Month: String,
    val Tithi: String,
    val Day: String,
    val Festival_English: String
) : Parcelable
