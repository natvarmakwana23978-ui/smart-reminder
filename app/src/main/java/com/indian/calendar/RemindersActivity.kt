package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

data class UserReminder(val time: String, val note: String)

class RemindersActivity : AppCompatActivity() {

    private lateinit var etNote: EditText
    private lateinit var etTime: EditText
    private lateinit var btnAdd: Button
    private lateinit var lvReminders: ListView
    private val remindersList = mutableListOf<UserReminder>()
    private lateinit var adapter: ArrayAdapter<String>

    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        etNote = findViewById(R.id.etNote)
        etTime = findViewById(R.id.etTime)
        btnAdd = findViewById(R.id.btnAddReminder)
        lvReminders = findViewById(R.id.lvReminders)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        lvReminders.adapter = adapter

        loadReminders()
        refreshList()

        btnAdd.setOnClickListener {
            val note = etNote.text.toString()
            val time = etTime.text.toString()
            if (note.isNotEmpty() && time.isNotEmpty()) {
                val reminder = UserReminder(time, note)
                remindersList.add(reminder)
                saveReminders()
                refreshList()
                CalendarWidget.updateAllWidgets(this)
            } else {
                Toast.makeText(this, "Enter time and note", Toast.LENGTH_SHORT).show()
            }
        }

        lvReminders.setOnItemLongClickListener { _, _, position, _ ->
            // delete on long click
            remindersList.removeAt(position)
            saveReminders()
            refreshList()
            CalendarWidget.updateAllWidgets(this)
            true
        }
    }

    private fun refreshList() {
        val list = remindersList.map { "${it.time} - ${it.note}" }
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private fun saveReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = remindersList.map { "${it.time}|${it.note}" }.toSet()
        prefs.edit().putStringSet("user_reminders", set).apply()
    }

    private fun loadReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("user_reminders", emptySet()) ?: emptySet()
        remindersList.clear()
        set.forEach {
            val parts = it.split("|")
            if (parts.size == 2) remindersList.add(UserReminder(parts[0], parts[1]))
        }
    }
}
