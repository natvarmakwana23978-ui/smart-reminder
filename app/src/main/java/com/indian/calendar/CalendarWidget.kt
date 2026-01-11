package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
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
        val views = RemoteViews(context.packageName, R.layout.calendar_widget)
        
        // ૧. તારીખ સેટ કરવી (date_line_1 માટે)
        val fullDateSdf = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
        views.setTextViewText(R.id.date_line_1, fullDateSdf.format(Date()))

        // ૨. ડેટાબેઝ ચેક કરવા માટેની તારીખ
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())

        thread {
            val db = AppDatabase.getDatabase(context)
            val noteEntry = db.userNoteDao().getNoteByDate(currentDate)
            
            // તમારી નોંધ 'date_line_4' માં બતાવવી
            val displayText = noteEntry?.note ?: "આજે કોઈ નોંધ નથી"
            
            views.setTextViewText(R.id.date_line_4, displayText)
            
            // વિજેટ અપડેટ કરો
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
