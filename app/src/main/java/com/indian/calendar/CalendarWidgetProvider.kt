package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.indian.calendar.model.CalendarDayData
import org.json.JSONArray
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val todayData = getTodayCalendarData(context)

        appWidgetIds.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)
            
            // તમારી નવી શીટના હેડર મુજબ ENGLISH અને Gujarati નો ઉપયોગ
            views.setTextViewText(R.id.line1, todayData.Date) // ENGLISH તારીખ
            views.setTextViewText(R.id.line2, todayData.Gujarati) // ગુજરાતી વિગત (તિથિ/તહેવાર)
            
            // લાઈન ૩ અને ૪ અત્યારે ખાલી રાખીએ છીએ અથવા બીજી કોઈ માહિતી માટે વાપરી શકાય
            views.setTextViewText(R.id.line3, "") 
            views.setTextViewText(R.id.line4, "") 

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }

    private fun getTodayCalendarData(context: Context): CalendarDayData {
        try {
            val jsonStream: InputStream = context.assets.open("calendar.json")
            val jsonText = jsonStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            val today = Calendar.getInstance()
            // તમારી શીટમાં તારીખ 1/1/2026 ફોર્મેટમાં છે
            val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
            val todayStr = sdf.format(today.time)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                // શીટના નવા હેડર ENGLISH મુજબ ચેક કરવું
                if (obj.optString("ENGLISH") == todayStr) {
                    return CalendarDayData(
                        Date = obj.optString("ENGLISH"),
                        Gujarati = obj.optString("ગુજરાતી (Gujarati)")
                    )
                }
            }
            return CalendarDayData(Date = todayStr, Gujarati = "ડેટા મળ્યો નથી")
        } catch (e: Exception) {
            return CalendarDayData(Date = "Error", Gujarati = e.message ?: "ભૂલ")
        }
    }
}
