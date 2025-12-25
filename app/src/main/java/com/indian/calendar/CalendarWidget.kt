package com.your.package.name // અહીં તમારું એક્ચ્યુઅલ પેકેજ નામ લખેલું જ હશે

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
        
        // ગીટહબ JSON લિંક (જાન્યુઆરી ૨૦૨૬ ડેટા)
        val jsonUrl = "https://raw.githubusercontent.com/user/repo/main/calendar_jan_2026.json"

        thread {
            try {
                val rawJson = URL(jsonUrl).readText()
                val jsonArray = JSONArray(rawJson)
                
                // ડેટા મેપિંગ - તમારા JSON ના કી-નામ મુજબ
                val data = jsonArray.getJSONObject(0) // ટેસ્ટિંગ માટે પહેલી એન્ટ્રી

                views.setTextViewText(R.id.txt_eng_date, data.optString("eng_date"))
                views.setTextViewText(R.id.txt_event_top, data.optString("event_title"))
                views.setTextViewText(R.id.txt_event_note, data.optString("event_note"))
                views.setTextViewText(R.id.txt_guj_tithi, data.optString("guj_tithi"))
                views.setTextViewText(R.id.txt_guj_event, data.optString("guj_event"))
                views.setTextViewText(R.id.txt_birthday_wish, data.optString("wish_note"))

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } catch (e: Exception) {
                // લોડ ન થાય તો ડિફોલ્ટ મેસેજ
                views.setTextViewText(R.id.txt_event_top, "ડેટા લોડ થઈ શક્યો નથી")
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
}
