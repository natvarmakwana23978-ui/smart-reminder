package com.indian.calendar

import android.icu.util.Calendar
import android.icu.util.ULocale
import android.icu.text.DateFormat
import java.util.Date

object GlobalCalendarManager {
    fun getFormattedDate(calendarType: String, languageCode: String): String {
        return try {
            val locale = ULocale("${languageCode}@calendar=${calendarType}")
            val calendar = Calendar.getInstance(locale)
            calendar.time = Date()
            
            val df = DateFormat.getDateInstance(DateFormat.FULL, locale)
            var result = df.format(calendar)

            // ધંધાકીય દ્રષ્ટિએ સુંદર ફોર્મેટિંગ
            if (languageCode == "gu") {
                when (calendarType) {
                    "indian" -> {
                        result = result.replace("Saka", "શક સંવત")
                        // ઉદાહરણ: "શક સંવત ૧૯૪૭, પોષ સુદ-૮, સોમવાર" (આ મુજબ સેટ થશે)
                    }
                    "islamic", "islamic-civil" -> {
                        result = "હિજરી " + result.replace("AH", "")
                    }
                    "persian" -> {
                        result = "પારસી રોજ: " + result
                    }
                }
                result = convertToLocalNumbers(result)
            }
            return result
        } catch (e: Exception) { "Error" }
    }

    fun convertToLocalNumbers(input: String): String {
        val en = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val gu = arrayOf("૦", "૧", "૨", "૩", "૪", "૫", "૬", "૭", "૮", "૯")
        var out = input
        for (i in 0..9) out = out.replace(en[i], gu[i])
        return out
    }
}
