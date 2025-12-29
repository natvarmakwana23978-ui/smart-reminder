package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget)
        val prefs = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
        
        // ૧. આજની તારીખ
        val now = Date()
        val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val todayKey = sdfKey.format(now)

        // લાઈન ૧: અંગ્રેજી (ફિક્સ)
        val line1 = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH).format(now)
        views.setTextViewText(R.id.date_line_1, line1)

        // સેટિંગ્સમાંથી ભાષા અને કેલેન્ડર પ્રકાર લો
        val lang = prefs.getString("language", "gu") ?: "gu"
        val selectedCal = "parsi_zoroastrian"

        // JSON ડેટા લોડ કરો
        val jsonData = loadJSONFromAsset(context)
        val dayObject = jsonData?.optJSONObject(todayKey)

        // લાઈન ૨: કેલેન્ડર વિગત (સ્માર્ટ લોજિક સાથે)
        var line2Text = ""
        if (selectedCal == "parsi_zoroastrian") {
            // જો પારસી હોય તો આપણે બનાવેલા એન્જિન માંથી સીધો ડેટા લો
            line2Text = CalendarEngine.getParsiDetails(now, lang)
        } else if (dayObject != null) {
            val globalCals = dayObject.optJSONObject("global_calendars")
            val rawValue = globalCals?.optString(selectedCal, "") ?: ""
            // અન્ય કેલેન્ડર માટે ફોર્મેટર વાપરો
            line2Text = CalendarEngine.formatCalendarDisplay(rawValue, selectedCal, lang)
        }
        views.setTextViewText(R.id.date_line_2, line2Text)

        // લાઈન ૩: તહેવાર (ભાષા મુજબ)
        val festivals = dayObject?.optJSONObject("festivals")
        val festivalName = festivals?.optString(lang) ?: dayObject?.optJSONObject("gujarati_info")?.optString("festival") ?: ""
        views.setTextViewText(R.id.date_line_3, if (festivalName.isEmpty()) "વિશેષ દિવસ નથી" else festivalName)

        // લાઈન ૪: રીમાઇન્ડર (ડેટાબેઝ માંથી આવશે, હાલ સેમ્પલ)
        val reminder = prefs.getString("reminder_$todayKey", "કોઈ રીમાઇન્ડર નથી")
        views.setTextViewText(R.id.date_line_4, reminder)

        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }

    private fun loadJSONFromAsset(context: Context): JSONObject? {
        return try {
            val stream = context.assets.open("json/calendar_data.json")
            val buffer = ByteArray(stream.available())
            stream.read(buffer)
            stream.close()
            JSONObject(String(buffer, Charsets.UTF_8))
        } catch (e: Exception) { null }
    }
}

