package com.indian.calendar

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var calendarListView: ListView
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private val calendarList = mutableListOf<CalendarItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view) // Make sure RecyclerView ID matches

        // Initialize RecyclerView
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView)
        calendarRecyclerView.layoutManager = GridLayoutManager(this, 7)
        calendarAdapter = CalendarAdapter(emptyList())
        calendarRecyclerView.adapter = calendarAdapter

        // Initialize ListView for calendar selection
        calendarListView = findViewById(R.id.calendarListView) // Add ListView in layout
        fetchCalendars()
    }

    private fun fetchCalendars() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.instance.getCalendars()
                calendarList.clear()
                calendarList.addAll(response)
                calendarListView.adapter = CalendarListAdapter(this@CalendarSelectionActivity, calendarList)

                // Handle selection
                calendarListView.setOnItemClickListener { _, _, position, _ ->
                    val selectedCalendar = calendarList[position]
                    val colIndex = position
                    PreferencesHelper.saveSelectedCalendar(this@CalendarSelectionActivity, selectedCalendar.calendarName, colIndex)
                    fetchCalendarDetails(colIndex)
                }

            } catch (e: Exception) {
                Toast.makeText(this@CalendarSelectionActivity, "Failed to fetch calendars", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchCalendarDetails(colIndex: Int) {
        if (colIndex == -1) return
        lifecycleScope.launch {
            try {
                val calendarData = RetrofitClient.instance.getCalendarData(colIndex)
                calendarAdapter.updateData(calendarData)
            } catch (e: Exception) {
                Toast.makeText(this@CalendarSelectionActivity, "Failed to fetch calendar details", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
