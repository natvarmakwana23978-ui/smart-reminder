package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    // આ ફંક્શન અંગ્રેજી આંકડાને ગુજરાતીમાં ફેરવશે
    private fun toGujaratiNumbers(input: String): String {
        val englishNumbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val gujaratiNumbers = arrayOf("૦", "૧", "૨", "૩", "૪", "૫", "૬", "૭", "૮", "૯")
        var result = input
        for (i in 0..9) {
            result = result.replace(englishNumbers[i], gujaratiNumbers[i])
        }
        return result
    }

    // મહિનાના નામોનું ભાષાંતર (અત્યારે નમૂના માટે થોડા નામ રાખ્યા છે)
    private fun translateMonth(month: String): String {
        return when (month) {
            "January" -> "જાન્યુઆરી"
            "Paush Sud" -> "પોષ સુદ"
            "Paush Vad" -> "પોષ વદ"
            "Paush Purnima" -> "પોષ પૂનમ"
            "Rajab" -> "રજબ"
            else -> month
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.widget_royal_layout)
            
            val sharedPref = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
            val selectedKey = sharedPref.getString("selected_key", "vikram_samvat") ?: "vikram_samvat"
            val showAllFestivals = sharedPref.getBoolean("show_all_festivals", false)
            
            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

            try {
                val inputStream = context.assets.open("json/calendar_2082.json")
                val jsonText = inputStream.bufferedReader().use { it.readText() }
                val rootObject = JSONObject(jsonText)

                if (rootObject.has(currentDate)) {
                    val dateData = rootObject.getJSONObject(currentDate)

                    // ૧. ગ્રેગોરિયન તારીખ (Card 1)
                    val greg = dateData.getJSONObject("calendars").getJSONObject("gregorian")
                    val engDisplay = "${toGujaratiNumbers(greg.getString("date"))} ${translateMonth(greg.getString("month"))} ${toGujaratiNumbers(greg.getString("year"))}"
                    views.setTextViewText(R.id.widget_english_date, engDisplay)

                    // ૨. પસંદ કરેલું કેલેન્ડર (Card 2)
                    val calObj = dateData.getJSONObject("calendars").getJSONObject(selectedKey)
                    val localDisplay = "${toGujaratiNumbers(calObj.getString("date"))} ${translateMonth(calObj.getString("month"))} ${toGujaratiNumbers(calObj.getString("year"))}"
                    views.setTextViewText(R.id.widget_date_text, localDisplay)

                    // ૩. ફિલ્ટર કરેલા તહેવારો (Card 3)
                    val festArray = dateData.getJSONArray("festivals")
                    var festToDisplay = ""
                    for (i in 0 until festArray.length()) {
                        val fest = festArray.getJSONObject(i)
                        // જો 'Show All' ચાલુ હોય અથવા કેટેગરી મેચ થતી હોય
                        if (showAllFestivals || fest.getString("category") == selectedKey) {
                            festToDisplay = fest.getString("name") // અહીં પણ ટ્રાન્સલેશન કરી શકાય
                            break
                        }
                    }
                    views.setTextViewText(R.id.widget_festival_text, festToDisplay)
                }
            } catch (e: Exception) {
                views.setTextViewText(R.id.widget_date_text, "Error")
            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

