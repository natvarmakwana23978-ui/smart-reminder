package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int) {

        val views = RemoteViews(context.packageName, R.layout.calendar_widget)
        val prefs = context.getSharedPreferences("CalendarPrefs", Context.MODE_PRIVATE)
        
        // ૧. યુઝરની પસંદગી મેળવો (ભાષા અને કેલેન્ડર પ્રકાર)
        // જો યુઝરે કઈ પસંદ ન કર્યું હોય તો ગુજરાતી (gu) અને ઇસ્લામિક (islamic) ડિફોલ્ટ રહેશે
        val selectedLang = prefs.getString("language", "gu") ?: "gu"
        val selectedCal = prefs.getString("calendar_type", "islamic") ?: "islamic"

        val now = Date()
        val todayKey = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(now)

        // --- લાઈન ૧: ગ્રેગોરિયન (અંગ્રેજી) તારીખ ---
        // આ લાઈન પણ યુઝરે પસંદ કરેલી ભાષામાં દેખાશે
        val df1 = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale(selectedLang))
        views.setTextViewText(R.id.date_line_1, df1.format(now))

        // --- લાઈન ૨: ગ્લોબલ કેલેન્ડર (વિશ્વની કોઈપણ ભાષામાં) ---
        // આ આપણું નવું ગ્લોબલ એન્જિન વાપરશે
        val line2Text = GlobalCalendarManager.getFormattedDate(selectedCal, selectedLang)
        views.setTextViewText(R.id.date_line_2, line2Text)

        // JSON ડેટા લોડ કરો (તહેવાર અને વિશેષ દિવસ માટે)
        val jsonData = loadJSONFromAsset(context)
        val dayData = jsonData?.optJSONObject(todayKey)

        // --- લાઈન ૩: તહેવાર / વિશેષ દિવસ ---
        val festival = dayData?.optJSONObject("festivals")?.optString(selectedLang) 
                       ?: dayData?.optJSONObject("gujarati_info")?.optString("festival") ?: ""
        
        val noFestivalText = if (selectedLang == "gu") "આજે કોઈ વિશેષ દિવસ નથી" else "No special events today"
        views.setTextViewText(R.id.date_line_3, if (festival.isEmpty()) noFestivalText else festival)

        // --- લાઈન ૪: યુઝરનું પર્સનલ રીમાઇન્ડર (નોંધ) ---
        val reminder = prefs.getString("reminder_$todayKey", "")
        val noReminderText = if (selectedLang == "gu") "કોઈ નોંધ નથી" else "No reminders"
        views.setTextViewText(R.id.date_line_4, if (reminder.isNullOrEmpty()) noReminderText else reminder)

        // વિજેટ અપડેટ કરો
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun loadJSONFromAsset(context: Context): JSONObject? {
        return try {
            val stream = context.assets.open("json/calendar_data.json")
            val buffer = ByteArray(stream.available())
            stream.read(buffer)
            stream.close()
            JSONObject(String(buffer, Charsets.UTF_8))
        } catch (e: Exception) {
            null
        }
    }
}
