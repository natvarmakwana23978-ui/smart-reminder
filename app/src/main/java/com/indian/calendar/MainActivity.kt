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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtGregorianDate: TextView
    private lateinit var txtSelectedCalendarDate: TextView
    private lateinit var txtSpecialDay: TextView
    private lateinit var calendarSpinner: Spinner
    private var calendarDataArray: JSONArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // વ્યૂ લિંક કરવા
        txtGregorianDate = findViewById(R.id.txtGregorianDate)
        txtSelectedCalendarDate = findViewById(R.id.txtVikramDate)
        txtSpecialDay = findViewById(R.id.txtSpecialDay)
        calendarSpinner = findViewById(R.id.calendarSpinner)

        // ૧૦ મુખ્ય કેલેન્ડર લિસ્ટ (તમારા JSON ની કી મુજબ)
        val calendars = arrayOf("Vikram_Samvat", "Jewish", "Hijri", "Saka", "Parsi", "Sikh")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendars)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calendarSpinner.adapter = adapter

        // સેવ કરેલી પસંદગી લોડ કરવી
        val sharedPref = getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
        val savedPos = sharedPref.getInt("selected_pos", 0)
        calendarSpinner.setSelection(savedPos)

        loadJSON()

        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedKey = calendars[position]
                
                // પસંદગી સેવ કરવી
                sharedPref.edit().putInt("selected_pos", position).apply()
                sharedPref.edit().putString("selected_key", selectedKey).apply()
                
                // UI અપડેટ કરવું
                updateUI(selectedKey)
                
                // વિજેટને અપડેટ કરવા માટેનો આદેશ (Broadcast)
                updateWidgetNow()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun loadJSON() {
        try {
            val inputStream = assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            calendarDataArray = JSONArray(jsonText)
        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun updateUI(selectedKey: String) {
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        
        calendarDataArray?.let {
            for (i in 0 until it.length()) {
                val obj = it.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    
                    // અંગ્રેજી તારીખ
                    val lang = if (selectedKey == "Vikram_Samvat") Locale("gu") else Locale.US
                    txtGregorianDate.text = SimpleDateFormat("dd MMMM yyyy (EEEE)", lang).format(Date())

                    // લોકલ કેલેન્ડર ડેટા + ટ્રાન્સલેશન
                    var rawData = obj.getString(selectedKey)
                    if (selectedKey != "Vikram_Samvat") {
                        rawData = rawData.replace("૧", "1").replace("૨", "2").replace("૩", "3")
                            .replace("૪", "4").replace("૫", "5").replace("૬", "6")
                            .replace("૭", "7").replace("૮", "8").replace("૯", "9").replace("૦", "0")
                    }
                    txtSelectedCalendarDate.text = rawData

                    // તહેવાર
                    txtSpecialDay.text = obj.getString("Special_Day")
                    break
                }
            }
        }
    }

    private fun updateWidgetNow() {
        val intent = Intent(this, CalendarWidget::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        val ids = AppWidgetManager.getInstance(application).getAppWidgetIds(ComponentName(application, CalendarWidget::class.java))
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(intent)
    }
}

