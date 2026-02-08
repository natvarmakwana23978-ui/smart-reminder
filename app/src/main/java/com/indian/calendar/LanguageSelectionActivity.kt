package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding // XML મુજબ નામ

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val selectedLang = binding.languageSpinner.selectedItem.toString()
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
