package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding

class CalendarSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCalendarSelectionBinding
    
    // ગૂગલ શીટ મુજબના ૨૭ કેલેન્ડર
    private val calendarList = listOf(
        "Vikram Samvat 2082", "Hijri 1447", "Bengali 1433", 
        "Marathi 1948", "Telugu 1948", "Gregorian 2026"
        // ... બાકીના નામ તમે અહીં ઉમેરી શકો છો
    )
    
    private val languageList = listOf("Gujarati", "Hindi", "English", "Marathi")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // સ્પિનર (Dropdown) સેટઅપ
        val calendarAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendarList)
        binding.calendarSpinner.adapter = calendarAdapter

        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languageList)
        binding.languageSpinner.adapter = langAdapter

        // બટન ક્લિક લોજિક
        binding.btnOpenCalendar.setOnClickListener {
            val selectedCal = binding.calendarSpinner.selectedItem.toString()
            val selectedLang = binding.languageSpinner.selectedItem.toString()

            // મેઈન કેલેન્ડર સ્ક્રીન પર ડેટા મોકલો
            val intent = Intent(this, MainActivity::class.java) 
            intent.putExtra("SELECTED_CALENDAR", selectedCal)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
