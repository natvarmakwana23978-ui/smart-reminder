package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    // આ ફંક્શન કોઈપણ ભાષાના આંકડા (1, 2, 3) ને તે ભાષાની લિપિમાં ફેરવશે
    private fun formatLocalNumbers(text: String, langCode: String): String {
        val locale = Locale(langCode)
        val nf = java.text.NumberFormat.getInstance(locale)
        var result = ""
        for (char in text) {
            result += if (char.isDigit()) {
                nf.format(char.toString().toLong())
            } else {
                char
            }
        }
        return result
    }

    // સિસ્ટમમાંથી વાર અને મહિનાના નામ ઓટોમેટિક મેળવવા માટે
    private fun getSystemName(calendar: Calendar, pattern: String, langCode: String): String {
        val sdf = SimpleDateFormat(pattern, Locale(langCode))
        return sdf.format(calendar.time)
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            
            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            val selectedLang = sharedPref.getString("selected_language", "gu") ?: "gu"
            
            val calendar = Calendar.getInstance()
            val dbSdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = dbSdf.format(calendar.time)

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧ (Gregorian) - સંપૂર્ણ ઓટોમેટિક ટ્રાન્સલેશન
                    val dayName = getSystemName(calendar, "EEEE", selectedLang)
                    val monthName = getSystemName(calendar, "MMMM", selectedLang)
                    val dateNum = formatLocalNumbers(calendar.get(Calendar.DAY_OF_MONTH).toString(), selectedLang)
                    val yearNum = formatLocalNumbers(calendar.get(Calendar.YEAR).toString(), selectedLang)
                    
                    val commonDate = "$dateNum $monthName, $yearNum - $dayName"
                    views.setTextViewText(R.id.widget_english_date, commonDate)

                    // ૨. કાર્ડ ૨ (Custom Calendar) - આંકડા ટ્રાન્સલેટ થશે
                    if (calendars.has(selectedKey)) {
                        val calObj = calendars.getJSONObject(selectedKey)
                        // વર્ષ અને તારીખના આંકડા બદલાશે, મહિનાનું નામ હાલ જેસોન મુજબ રહેશે
                        val localInfo = "${formatLocalNumbers(calObj.getString("year"), selectedLang)}, ${calObj.getString("month")} - ${formatLocalNumbers(calObj.getString("date"), selectedLang)}"
                        views.setTextViewText(R.id.widget_date_text, localInfo)
                    }

                    // ૩. તહેવારો
                    val festArray = dateData.optJSONArray("festivals")
                    if (festArray != null && festArray.length() > 0) {
                        views.setViewVisibility(R.id.widget_festival_text, View.VISIBLE)
                        views.setTextViewText(R.id.widget_festival_text, festArray.getJSONObject(0).getString("name"))
                    } else {
                        views.setViewVisibility(R.id.widget_festival_text, View.GONE)
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "Data Error")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

