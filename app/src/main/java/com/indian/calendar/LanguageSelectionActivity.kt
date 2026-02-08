package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityLanguageSelectionBinding

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLanguageSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            // Spinner માંથી સ્ટ્રિંગ મેળવો
            val selectedLang: String = binding.languageSpinner.selectedItem.toString()
            
            val intent = Intent(this, CalendarSelectionActivity::class.java)
            intent.putExtra("SELECTED_LANG", selectedLang)
            startActivity(intent)
        }
    }
}
