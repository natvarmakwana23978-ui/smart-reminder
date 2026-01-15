package com.indian.calendar

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class UserReminder(val time: String, val note: String) // simple model

class RemindersActivity : AppCompatActivity() {

    private lateinit var etNote: EditText
    private lateinit var etTime: EditText
    private lateinit var btnAdd: Button
    private lateinit var lvReminders: ListView
    private val remindersList = mutableListOf<UserReminder>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)

        etNote = findViewById(R.id.etNote)
        etTime = findViewById(R.id.etTime)
        btnAdd = findViewById(R.id.btnAddReminder)
        lvReminders = findViewById(R.id.lvReminders)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, remindersList.map { "${it.time} - ${it.note}" })
        lvReminders.adapter = adapter

        loadReminders()

        btnAdd.setOnClickListener {
            val note = etNote.text.toString()
            val time = etTime.text.toString()
            if (note.isNotEmpty() && time.isNotEmpty()) {
                val reminder = UserReminder(time, note)
                remindersList.add(reminder)
                saveReminders()
                refreshList()
                updateWidget()
            } else {
                Toast.makeText(this, "Enter time and note", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun refreshList() {
        adapter.clear()
        adapter.addAll(remindersList.map { "${it.time} - ${it.note}" })
        adapter.notifyDataSetChanged()
    }

    private fun saveReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = remindersList.map { "${it.time}|${it.note}" }.toSet()
        prefs.edit().putStringSet("user_reminders", set).apply()
    }

    private fun loadReminders() {
        val prefs = getSharedPreferences("RemindersPrefs", Context.MODE_PRIVATE)
        val set = prefs.getStringSet("user_reminders", emptySet())
        remindersList.clear()
        set?.forEach {
            val parts = it.split("|")
            if (parts.size == 2) remindersList.add(UserReminder(parts[0], parts[1]))
        }
        refreshList()
    }

    private fun updateWidget() {
        // trigger widget update
        CalendarWidget.updateAllWidgets(this)
    }
}
