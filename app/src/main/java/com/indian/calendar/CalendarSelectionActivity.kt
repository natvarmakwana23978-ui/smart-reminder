package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.indian.calendar.databinding.ActivityCalendarSelectionBinding

class CalendarSelectionActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityCalendarSelectionBinding
    private lateinit var adapter: CalendarSelectionAdapter
    
    // ગૂગલ શીટમાંથી 27 કેલેન્ડર (સેમ્પલ)
    private val calendarList = listOf(
        CalendarItem("1", "Vikram Samvat 2082", "2026-2027", true),
        CalendarItem("2", "Hijri 1447", "2025-2026", true),
        CalendarItem("3", "Bengali 1433", "2026-2027", true),
        CalendarItem("4", "Marathi 1948", "2026", true),
        CalendarItem("5", "Telugu 1948", "2026", true),
        CalendarItem("6", "Hebrew 5786", "2025-2026", true),
        CalendarItem("7", "Chinese 4724", "2026", true),
        // ... 20 more
        CalendarItem("27", "Gregorian 2026", "2026", true)
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupRecyclerView()
        setupClickListeners()
    }
    
    private fun setupRecyclerView() {
        adapter = CalendarSelectionAdapter(calendarList) { calendar ->
            // કેલેન્ડર સિલેક્ટ કર્યા પછી
            val intent = Intent(this, LanguageSelectionActivity::class.java)
            intent.putExtra("CALENDAR_ID", calendar.id)
            intent.putExtra("CALENDAR_NAME", calendar.name)
            startActivity(intent)
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
    
    private fun setupClickListeners() {
        // "Create New Calendar" બટન
        binding.btnCreateNew.setOnClickListener {
            startActivity(Intent(this, CreateCalendarActivity::class.java))
        }
    }
}

data class CalendarItem(
    val id: String,
    val name: String,
    val year: String,
    val isPreloaded: Boolean
)
