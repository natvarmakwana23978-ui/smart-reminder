package com.indian.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNote(
    @PrimaryKey val dateKey: String, // ફોર્મેટ: "dd/MM" (દા.ત. "05/01")
    val personalNote: String,       // "આજે લાયસન્સ રીન્યુ કરાવો"
    val customPanchang: String? = null,    // જો યુઝરે તિથિ સુધારી હોય તો
    val customFestival: String? = null,    // જો યુઝરે તહેવાર સુધાર્યો હોય તો
    val advanceDays: Int = 10       // કેટલા દિવસ અગાઉ રીમાઇન્ડર બતાવવું
)
