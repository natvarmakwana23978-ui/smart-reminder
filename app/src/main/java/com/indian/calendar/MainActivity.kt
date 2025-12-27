package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

        // ૧. કેલેન્ડર સ્પિનર સેટઅપ
        val calSpinner: Spinner = findViewById(R.id.calendar_spinner)
        val calOptions = arrayOf("વિક્રમ સંવત", "શક સંવત", "હિજરી", "નાનકશાહી", "ચાઈનીઝ")
        val calKeys = arrayOf("vikram_samvat", "shaka_samvat", "hijri", "nanakshahi", "chinese")
        
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calOptions)
        calAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calSpinner.adapter = calAdapter

        // ૨. ભાષા સ્પિનર સેટઅપ
        val langSpinner: Spinner = findViewById(R.id.language_spinner)
        val langOptions = arrayOf("English", "ગુજરાતી", "Hindi")
        val langCodes = arrayOf("en", "gu", "hi")
        
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, langOptions)
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        langSpinner.adapter = langAdapter

        // અગાઉનું સિલેક્શન લોડ કરો
        val savedCal = sharedPref.getString("selected_key", "vikram_samvat")
        calSpinner.setSelection(calKeys.indexOf(savedCal))

        val savedLang = sharedPref.getString("selected_language", "gu")
        langSpinner.setSelection(langCodes.indexOf(savedLang))

        // કેલેન્ડર બદલવા માટેનું લોજિક
        calSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                sharedPref.edit().putString("selected_key", calKeys[pos]).commit()
                updateWidget()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }

        // ભાષા બદલવા માટેનું લોજિક
        langSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p: AdapterView<*>?, v: View?, pos: Int, id: Long) {
                sharedPref.edit().putString("selected_language", langCodes[pos]).commit()
                updateWidget()
            }
            override fun onNothingSelected(p: AdapterView<*>?) {}
        }
    }

    // વિજેટને તરત જ રિફ્રેશ કરવાનું ફંક્શન
    private fun updateWidget() {
        val intent = Intent(this, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(
            ComponentName(application, CalendarWidget::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}
