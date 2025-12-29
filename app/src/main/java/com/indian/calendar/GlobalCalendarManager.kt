 package com.indian.calendar

import android.icu.util.Calendar
import android.icu.util.ULocale
import android.icu.text.DateFormat
import java.util.Date

object GlobalCalendarManager {
    /**
     * @param calendarType: "islamic", "persian", "hebrew", "indian", "chinese", "ethiopic"
     * @param languageCode: યુઝરે પસંદ કરેલ કોડ જેમ કે "gu", "en", "hi", "fr", "es"
     */
    fun getFormattedDate(calendarType: String, languageCode: String): String {
        return try {
            // યુઝરની પસંદગી મુજબ લોકેલ સેટ કરો (ફોનનું લોકેશન ધ્યાને લેવાશે નહીં)
            val locale = ULocale("${languageCode}@calendar=${calendarType}")
            val calendar = Calendar.getInstance(locale)
            calendar.time = Date()
            
            // પૂરા ફોર્મેટમાં તારીખ (મહિનાના નામ અને વાર સાથે)
            val df = DateFormat.getDateInstance(DateFormat.FULL, locale)
            df.format(calendar)
        } catch (e: Exception) {
            "Error: Calendar not supported"
        }
    }
}

