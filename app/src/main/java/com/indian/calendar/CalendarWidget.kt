package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import java.net.HttpURLConnection
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

        thread {
            try {
                // તમારી સાચી Raw GitHub લિંક
                val jsonUrl = "https://raw.githubusercontent.com/natvarmakwana23978-ui/indian-calendar-app/main/app/src/main/assets/json/calendar_2082.json"
                
                val url = URL(jsonUrl)
                val connection = url.openConnection() as HttpURLConnection
                
                // આ લાઈન ગિટહબ સુરક્ષા પાસ કરવા માટે જરૂરી છે
                connection.setRequestProperty("User-Agent", "Mozilla/5.0")
                connection.connectTimeout = 10000 // 10 સેકન્ડ વેટ કરશે
                connection.readTimeout = 10000

                val jsonText = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)

                // આજના દિવસની તારીખ (YYYY-MM-DD)
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
                    views.setTextViewText(R.id.widget_date_text, "ડેટા નથી ($currentDate)")
                    views.setTextViewText(R.id.widget_festival_text, "તારીખ ચેક કરો")
                }

                appWidgetManager.updateAppWidget(appWidgetId, views)

            } catch (e: Exception) {
                // જો નેટ સ્લો હોય કે કોઈ એરર હોય
                views.setTextViewText(R.id.widget_date_text, "લોડિંગ ભૂલ...")
                views.setTextViewText(R.id.widget_festival_text, "નેટ કનેક્શન તપાસો")
                appWidgetManager.updateAppWidget(appWidgetId, views)
                e.printStackTrace()
            }
        }
    }
}
