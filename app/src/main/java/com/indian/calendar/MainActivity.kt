package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૧. ભાષા પસંદગી (Language Codes)
        val languages = arrayOf("gu", "hi", "en", "fr", "es", "ar", "ja")
        val langDropdown = findViewById<AutoCompleteTextView>(R.id.langDropdown)
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        langDropdown.setAdapter(langAdapter)
        langDropdown.setOnItemClickListener { _, _, position, _ ->
            prefs.edit().putString("language", languages[position]).apply()
            updateWidget() // વિજેટ તરત અપડેટ થશે
        }

        // ૨. કેલેન્ડર પ્રકાર (૨૨ કેલેન્ડર માંથી મુખ્ય)
        val calendars = arrayOf("islamic", "persian", "hebrew", "indian", "chinese", "coptic", "ethiopic")
        val calDropdown = findViewById<AutoCompleteTextView>(R.id.calDropdown)
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, calendars)
        calDropdown.setAdapter(calAdapter)
        calDropdown.setOnItemClickListener { _, _, position, _ ->
            prefs.edit().putString("calendar_type", calendars[position]).apply()
            updateWidget()
        }

        // ૩. તમામ મુખ્ય તહેવારો બતાવવા કે નહીં (Switch)
        val switchAllFestivals = findViewById<SwitchMaterial>(R.id.switchAllFestivals)
        switchAllFestivals.isChecked = prefs.getBoolean("show_all_festivals", false)
        switchAllFestivals.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("show_all_festivals", isChecked).apply()
            updateWidget()
        }

        // ૪. વિશેષ દિવસો બતાવવા કે નહીં (Switch)
        val switchSpecialDays = findViewById<SwitchMaterial>(R.id.switchSpecialDays)
        switchSpecialDays.isChecked = prefs.getBoolean("show_special_days", true)
        switchSpecialDays.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("show_special_days", isChecked).apply()
            updateWidget()
        }
    }

    // આ ફંક્શન વિજેટને તરત જ નવો ડેટા બતાવવા મજબૂર કરશે
    private fun updateWidget() {
        val intent = Intent(this, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application)
            .getAppWidgetIds(ComponentName(application, CalendarWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}

