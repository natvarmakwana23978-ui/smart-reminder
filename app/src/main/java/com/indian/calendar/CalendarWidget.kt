package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)

            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            val targetLang = sharedPref.getString("selected_language", TranslateLanguage.ENGLISH) ?: TranslateLanguage.ENGLISH

            val calendar = Calendar.getInstance()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val currentDate = sdf.format(calendar.time)

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)
                    val calendars = dateData.getJSONObject("calendars")

                    // ૧. કાર્ડ ૧ (English Date) - આને હંમેશા અંગ્રેજી રાખવાનું છે
                    val greg = calendars.getJSONObject("gregorian")
                    val englishDate = "${greg.getString("date")} ${greg.getString("month")}, ${greg.getString("year")} - ${greg.getString("day")}"
                    views.setTextViewText(R.id.widget_english_date, englishDate)

                    // ૨. કાર્ડ ૨ અને ૩ (જેને ટ્રાન્સલેટ કરવાનું છે)
                    val calObj = calendars.getJSONObject(selectedKey)
                    val festivalArray = dateData.optJSONArray("festivals")
                    val festName = if (festivalArray != null && festivalArray.length() > 0) festivalArray.getJSONObject(0).getString("name") else ""
                    
                    val rawTextToTranslate = "${calObj.getString("month")} - ${calObj.getString("date")}\n$festName"

                    if (targetLang != TranslateLanguage.ENGLISH) {
                        val options = TranslatorOptions.Builder()
                            .setSourceLanguage(TranslateLanguage.ENGLISH)
                            .setTargetLanguage(targetLang)
                            .build()
                        val translator = Translation.getClient(options)
                        
                        translator.translate(rawTextToTranslate)
                            .addOnSuccessListener { translated ->
                                views.setTextViewText(R.id.widget_date_text, translated)
                                appWidgetManager.updateAppWidget(appWidgetId, views)
                            }
                            .addOnFailureListener {
                                views.setTextViewText(R.id.widget_date_text, rawTextToTranslate)
                                appWidgetManager.updateAppWidget(appWidgetId, views)
                            }
                    } else {
                        views.setTextViewText(R.id.widget_date_text, rawTextToTranslate)
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            } catch (e: Exception) {
                // Error silent
            }
        }
    }
}

