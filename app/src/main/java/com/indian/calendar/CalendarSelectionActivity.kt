package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class CalendarSelectionActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private val calendarList = mutableListOf<CalendarModel>()
    private val webAppUrl = "તમારી_GOOGLE_SHEET_URL_અહીં"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        
        // ૧. ગૂગલ શીટમાંથી લિસ્ટ મેળવવું
        fetchCalendars()

        // ૨. નવું કેલેન્ડર બનાવવાનો ઓપ્શન (પગલું ૩)
        btnCreate.setOnClickListener {
            startActivity(Intent(this, ManageCalendarActivity::class.java))
        }
    }

    private fun fetchCalendars() {
        progressBar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
            { response ->
                progressBar.visibility = View.GONE
                for (i in 0 until response.length()) {
                    val item = response.getJSONObject(i)
                    calendarList.add(CalendarModel(
                        item.getString("calendarName"),
                        item.getString("creatorName")
                    ))
                }
                recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                    // કેલેન્ડર પસંદ કર્યા પછી ભાષા પસંદગી પર જવું (પગલું ૫)
                    val intent = Intent(this, LanguageSelectionActivity::class.java)
                    intent.putExtra("CALENDAR_NAME", selected.name)
                    startActivity(intent)
                }
            },
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "લિસ્ટ લોડ કરવામાં ભૂલ આવી", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }
}

