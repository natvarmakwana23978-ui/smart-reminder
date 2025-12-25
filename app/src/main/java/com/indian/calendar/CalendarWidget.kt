package com.natvarmakwana23978_ui.indian_calendar_app

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONArray
import java.net.URL
import kotlin.concurrent.thread

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateWidgetData(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidgetData(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.calendar_widget_layout)
        
        // તમારી GitHub JSON Raw Link
        val jsonUrl = "https://raw.githubusercontent.com/natvarmakwana23978-ui/indian-calendar-app/main/app/src/main/assets/json/calendar_2082.json"

        thread {
            try {
                val rawJson = URL(jsonUrl).readText()
                val jsonArray = JSONArray(rawJson)
                
                // હાલ પૂરતું ટેસ્ટિંગ માટે જાન્યુઆરી ૧, ૨૦૨૬ નો ડેટા (Index 0) લઈએ છીએ
                val data = jsonArray.getJSONObject(0)

                // JSON Keys: Date, Vikram_Samvat, Special_Day, Saka વગેરે
                views.setTextViewText(R.id.txt_eng_date, data.optString("Date"))
                views.setTextViewText(R.id.txt_event_top, data.optString("Special_Day"))
                views.setTextViewText(R.id.txt_event_note, "Saka: " + data.optString("Saka"))
                
                views.setTextViewText(R.id.txt_guj_tithi, data.optString("Vikram_Samvat"))
                views.setTextViewText(R.id.txt_guj_event, data.optString("Special_Day"))
                views.setTextViewText(R.id.txt_birthday_wish, "Happy Birthday Gemini!") // હાલ સ્ટેટિક

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                views.setTextViewText(R.id.txt_event_top, "Error: JSON લોડ થયો નથી")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
