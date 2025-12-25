package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)

        // ઈન્ટરનેટ પરથી ડેટા લાવવા માટે અલગ થ્રેડ વાપરવો પડે
        thread {
            try {
                // તમારા ગિટહબ JSON ની RAW લિંક (અહીં તમારી સાચી લિંક મૂકવી)
                val jsonUrl = "https://github.com/natvarmakwana23978-ui/indian-calendar-app/blob/main/app/src/main/assets/json/calendar_2082.json"
                val jsonText = URL(jsonUrl).readText()
                val jsonArray = JSONArray(jsonText)

                // આજના દિવસની તારીખ મેળવો (YYYY-MM-DD)
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())

                var found = false
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        val vs = obj.getString("Vikram_Samvat")
                        val special = obj.getString("Special_Day")
                        
                        views.setTextViewText(R.id.widget_date_text, vs)
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)
                        found = true
                        break
                    }
                }

                if (!found) {
                    views.setTextViewText(R.id.widget_date_text, "ડેટા મળ્યો નથી")
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "લોડિંગ ભૂલ...")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
