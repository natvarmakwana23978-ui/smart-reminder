package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val prefs = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૨૨ મુખ્ય ભાષાઓના કોડ
        val languages = arrayOf("gu", "hi", "mr", "pa", "bn", "ta", "te", "kn", "ml", "en", "es", "fr", "de", "it", "ru", "ar", "ja", "zh", "ko", "tr", "pt", "vi")
        
        // ૨૨ મુખ્ય કેલેન્ડરના કોડ
        val calendars = arrayOf("indian", "islamic", "persian", "hebrew", "chinese", "ethiopic", "coptic", "buddhist", "japanese", "roc", "iso8601", "gregorian", "dangi", "ancient", "islamic-civil", "islamic-tbla", "islamic-umalqura", "islamic-rgsa", "persian-civil", "hebrew-civil", "indian-civil", "ethiopic-amete-alem")

        val spinnerLanguage = findViewById<Spinner>(R.id.spinnerLanguage)
        val spinnerCalendar = findViewById<Spinner>(R.id.spinnerCalendar)
        val switchAllFestivals = findViewById<SwitchCompat>(R.id.switchAllFestivals)
        val switchSpecialDays = findViewById<SwitchCompat>(R.id.switchSpecialDays)

        spinnerLanguage.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        spinnerCalendar.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)

        // સેવ કરેલી વેલ્યુ સેટ કરવી
        spinnerLanguage.setSelection(languages.indexOf(prefs.getString("language", "gu")))
        spinnerCalendar.setSelection(calendars.indexOf(prefs.getString("calendar_type", "indian")))
        switchAllFestivals.isChecked = prefs.getBoolean("show_all_festivals", false)
        switchSpecialDays.isChecked = prefs.getBoolean("show_special_days", true)

        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val editor = prefs.edit()
            editor.putString("language", spinnerLanguage.selectedItem.toString())
            editor.putString("calendar_type", spinnerCalendar.selectedItem.toString())
            editor.putBoolean("show_all_festivals", switchAllFestivals.isChecked)
            editor.putBoolean("show_special_days", switchSpecialDays.isChecked)
            editor.apply()

            updateWidget(this)
            android.widget.Toast.makeText(this, "સેટિંગ્સ સેવ થયા!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateWidget(context: Context) {
        val intent = Intent(context, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(ComponentName(context, CalendarWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        context.sendBroadcast(intent)
    }
}
