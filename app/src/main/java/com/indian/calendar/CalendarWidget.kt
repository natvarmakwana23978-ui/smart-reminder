package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            // અહીં આપણે નવું નામ 'widget_royal_layout' વાપર્યું છે
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)

            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "Vikram_Samvat") ?: "Vikram_Samvat"

            val lang = if (selectedKey == "Vikram_Samvat") Locale("gu") else Locale.US
            val englishDateStr = SimpleDateFormat("dd MMM yyyy (EEEE)", lang).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        
                        var localData = obj.getString(selectedKey)
                        if (selectedKey != "Vikram_Samvat") {
                            localData = localData.replace("૧", "1").replace("૨", "2").replace("૩", "3")
                                .replace("૪", "4").replace("૫", "5").replace("૬", "6")
                                .replace("૭", "7").replace("૮", "8").replace("૯", "9").replace("૦", "0")
                        }
                        
                        views.setTextViewText(R.id.widget_date_text, "$englishDateStr\n$localData")

                        val special = obj.getString("Special_Day")
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)

                        // આપણે અહીં રીમાઇન્ડર ટેક્સ્ટની લાઇન કાઢી નાખી છે જેથી એરર ન આવે
                        break
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, englishDateStr)
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

