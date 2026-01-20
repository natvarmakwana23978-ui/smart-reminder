package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // આપણે સીધા જ કેલેન્ડર લિસ્ટ વાળી સ્ક્રીન પર જઈશું, જેથી કોઈ બટનની માથાકૂટ ન રહે [cite: 2026-01-20]
        val intent = Intent(this, CalendarSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }
}
