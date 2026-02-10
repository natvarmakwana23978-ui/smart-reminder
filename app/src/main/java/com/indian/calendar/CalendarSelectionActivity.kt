package com.smart.reminder
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.smart.reminder.databinding.ActivityCalendarSelectionBinding

class CalendarSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarSelectionBinding
    private val calendars = arrayOf("ENGLISH", "ગુજરાતી (Gujarati)", "हिन्दी (Hindi)", "ઇસ્લામિક (Islamic)", "తెలుగు/ಕನ್ನಡ (Telugu/Kannada)", "தமிழ் (Tamil)", "മലയാളം (Malayalam)", "ਪੰਜਾਬੀ (Punjabi)", "ଓଡ଼િଆ (Odia)", "বাংলা (Bengali)", "नेपाली (Nepali)", "中文 (Chinese)", "עברית (Hebrew)", "فارસી (Persian)", "ኢትዮጵያ (Ethiopian)", "Basa Bali (Balinese)", "한국어 (Korean)", "Tiếng Việt (Vietnamese)", "ไทย (Thai)", "Français (French)", "မြန်မာဘာသာ (Burmese)", "کاشمیری (Kashmiri)", "મારવાડી (Marwari)", "日本語 (Japanese)", "অસમীયા (Assamese)", "سنڌી (Sindhi)", "བོད་སྐદ (Tibetan)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.calendarSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        binding.btnOpenCalendar.setOnClickListener {
            val intent = Intent(this, CalendarViewActivity::class.java)
            intent.putExtra("LANG_INDEX", binding.calendarSpinner.selectedItemPosition)
            intent.putExtra("LANG_NAME", calendars[binding.calendarSpinner.selectedItemPosition])
            startActivity(intent)
        }
    }
}
