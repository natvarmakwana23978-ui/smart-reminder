package com.indian.calendar

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import java.text.SimpleDateFormat
import java.util.*

class CalendarWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (widgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, widgetId)
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.calendar_widget)

            // English date
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val today = dateFormat.format(Date())
            views.setTextViewText(R.id.tvWidgetDate, today)

            // Local calendar info – for demo, fetch from SharedPreferences
            val colIndex = PreferencesHelper.getSelectedColIndex(context)
            val tithi = if (colIndex != -1) "Purnima" else ""
            views.setTextViewText(R.id.tvWidgetTithi, tithi)

            // Next reminder – for demo, static
            val nextReminder = "Next: Study 6 PM" // Replace with dynamic logic
            views.setTextViewText(R.id.tvWidgetReminder, nextReminder)

            // Optional: open app on click
            val intent = Intent(context, CalendarSelectionActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget_root, pendingIntent)

            appWidgetManager.updateAppWidget(widgetId, views)
        }
    }
}
