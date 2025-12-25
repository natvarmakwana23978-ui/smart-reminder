package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.R
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)
            
            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonText)

                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val currentDate = sdf.format(Date())
                val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())

                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    if (obj.getString("Date") == currentDate) {
                        // લાઈન ૧: હિબ્રુ તારીખ અને વાર
                        views.setTextViewText(R.id.widget_date_text, "${obj.getString("Jewish")} ($dayName)")
                        
                        // લાઈન ૨: તહેવાર
                        val special = obj.getString("Special_Day")
                        views.setTextViewText(R.id.widget_festival_text, if (special == "--") "" else special)
                        
                        // લાઈન ૩: રીમાઇન્ડર (અત્યારે સ્ટેટિક ટેસ્ટિંગ માટે)
                        views.setTextViewText(R.id.widget_reminder_text, "રીમાઇન્ડર: આજે સમયસર દવા લેવી")
                        break
                    }
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "Error")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
