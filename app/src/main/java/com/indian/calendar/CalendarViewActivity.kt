package com.indian.calendar

import android.app.AlertDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.indian.calendar.databinding.ActivityCalendarViewBinding
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCalendarViewBinding
    private lateinit var adapter: DaysAdapter
    private var currentMonth = Calendar.getInstance().get(Calendar.MONTH)
    private var currentYear = 2026
    
    // રીમાઇન્ડર ડેટાબેસ (સિમ્યુલેશન)
    private val remindersMap = mutableMapOf<String, MutableList<ReminderItem>>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val calendarId = intent.getStringExtra("CALENDAR_ID") ?: "1"
        val languageCode = intent.getStringExtra("LANGUAGE_CODE") ?: "en"
        
        setupToolbar(calendarId, languageCode)
        setupCalendar()
        setupClickListeners()
    }
    
    private fun setupToolbar(calendarId: String, languageCode: String) {
        binding.toolbar.title = when(calendarId) {
            "1" -> "विक्रम संवत २०८२" // ભાષા અનુસાર બદલાશે
            "2" -> "Hijri 1447"
            else -> "Calendar $calendarId"
        }
        
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }
    
    private fun setupCalendar() {
        // Days for current month
        val days = generateDaysForMonth(currentMonth, currentYear)
        
        adapter = DaysAdapter(days) { day ->
            showDayDetailsDialog(day)
        }
        
        binding.recyclerView.layoutManager = GridLayoutManager(this, 7)
        binding.recyclerView.adapter = adapter
        
        updateMonthTitle()
    }
    
    private fun generateDaysForMonth(month: Int, year: Int): List<CalendarDayData> {
        val days = mutableListOf<CalendarDayData>()
        val calendar = Calendar.getInstance().apply {
            set(year, month, 1)
        }
        
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        
        // Empty days for first week
        repeat(firstDayOfWeek - 1) {
            days.add(CalendarDayData("", com.google.gson.JsonObject(), isEmpty = true))
        }
        
        // Actual days
        for (day in 1..daysInMonth) {
            val dateStr = "$year-${month+1}-$day"
            val englishDate = SimpleDateFormat("d MMM", Locale.US).format(
                Calendar.getInstance().apply { set(year, month, day) }.time
            )
            
            val allData = com.google.gson.JsonObject().apply {
                addProperty("gujaratiTithi", getTithiForDay(day, month))
                addProperty("festival", getFestivalForDay(dateStr))
            }
            
            val isWeekend = isWeekend(day, month, year)
            val reminders = remindersMap[dateStr] ?: emptyList()
            
            days.add(
                CalendarDayData(
                    englishDate = englishDate,
                    allData = allData,
                    colorCode = if (isWeekend) 1 else 0,
                    isWeekend = isWeekend,
                    hasReminders = reminders.isNotEmpty()
                )
            )
        }
        
        return days
    }
    
    private fun getTithiForDay(day: Int, month: Int): String {
        // તમે ગુજરાતી તિથિ ભરશો
        val tithis = listOf(
            "પ્રતિપદા", "દ્વિતિયા", "તૃતિયા", "ચતુર્થી", "પંચમી",
            "ષષ્ઠી", "સપ્તમી", "અષ્ટમી", "નવમી", "દશમી"
        )
        return tithis[(day + month) % 10]
    }
    
    private fun getFestivalForDay(dateStr: String): String {
        // તહેવારો માટે
        return when(dateStr) {
            "2026-2-18" -> "આજે એકાદશી છે"
            "2026-2-20" -> "વલ્ર્ડ ડે ઓફ સોશીયલ જસ્ટીશ"
            else -> ""
        }
    }
    
    private fun isWeekend(day: Int, month: Int, year: Int): Boolean {
        val calendar = Calendar.getInstance().apply {
            set(year, month, day)
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }
    
    private fun updateMonthTitle() {
        val monthNames = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        binding.tvMonthYear.text = "${monthNames[currentMonth]} $currentYear"
    }
    
    private fun setupClickListeners() {
        binding.btnPrevMonth.setOnClickListener {
            changeMonth(-1)
        }
        
        binding.btnNextMonth.setOnClickListener {
            changeMonth(1)
        }
        
        binding.btnAddReminder.setOnClickListener {
            showAddReminderDialog()
        }
    }
    
    private fun changeMonth(offset: Int) {
        currentMonth += offset
        if (currentMonth < 0) {
            currentMonth = 11
            currentYear--
        } else if (currentMonth > 11) {
            currentMonth = 0
            currentYear++
        }
        
        setupCalendar()
    }
    
    private fun showDayDetailsDialog(day: CalendarDayData) {
        if (day.isEmpty) return
        
        val dateStr = "$currentYear-${currentMonth+1}-${day.englishDate.split(" ")[0]}"
        val reminders = remindersMap[dateStr] ?: emptyList()
        
        AlertDialog.Builder(this)
            .setTitle("${day.englishDate} $currentYear")
            .setMessage(
                "તિથિ: ${day.allData.get("gujaratiTithi").asString}\n" +
                "તહેવાર: ${day.allData.get("festival").asString}\n\n" +
                "રીમાઇન્ડર્સ:\n${reminders.joinToString("\n") { it.text }}"
            )
            .setPositiveButton("Add Reminder") { dialog, _ ->
                showAddReminderForDateDialog(dateStr)
                dialog.dismiss()
            }
            .setNegativeButton("Close") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showAddReminderDialog() {
        val editText = EditText(this)
        editText.hint = "Enter reminder text..."
        
        AlertDialog.Builder(this)
            .setTitle("Add Reminder")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val text = editText.text.toString()
                if (text.isNotBlank()) {
                    // Save to database
                    // For now, simulate
                    val dateStr = "$currentYear-${currentMonth+1}-1"
                    remindersMap.getOrPut(dateStr) { mutableListOf() }
                        .add(ReminderItem(text = text))
                    
                    // Update widget
                    updateWidget()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun showAddReminderForDateDialog(dateStr: String) {
        val editText = EditText(this)
        editText.hint = "Reminder for $dateStr"
        
        AlertDialog.Builder(this)
            .setTitle("Add Reminder for $dateStr")
            .setView(editText)
            .setPositiveButton("Add") { dialog, _ ->
                val text = editText.text.toString()
                if (text.isNotBlank()) {
                    remindersMap.getOrPut(dateStr) { mutableListOf() }
                        .add(ReminderItem(text = text))
                    
                    // Refresh calendar
                    setupCalendar()
                    updateWidget()
                    dialog.dismiss()
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun updateWidget() {
        // Widget update logic
        // Send broadcast to widget
    }
}

data class ReminderItem(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val date: String = ""
)
