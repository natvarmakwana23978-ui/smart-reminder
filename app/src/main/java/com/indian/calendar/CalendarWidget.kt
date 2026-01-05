package com.indian.calendar

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val views = RemoteViews(context.packageName, R.layout.widget_layout)
        val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val displayDate = SimpleDateFormat("d MMM, EEE", Locale.getDefault()).format(Date())

        views.setTextViewText(R.id.widgetDate, displayDate)

        // ડેટાબેઝમાંથી નોંધ લોડ કરો
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context)
            val note = db.userNoteDao().getNoteByDate(today)
            
            views.setTextViewText(R.id.widgetNote, note?.personalNote ?: "આજનો દિવસ શુભ રહે!")
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
