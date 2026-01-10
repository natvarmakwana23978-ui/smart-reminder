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
    
    // તમારી નવી અને ટેસ્ટ કરેલી સાચી URL
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbw4BxpTd8aZEMmqVkgtVXdpco8mxBu1E9ikjKkdLdRHjBpn4QPRhMM-HCg0WsVPdGqimA/exec"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarListRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        val btnCreate = findViewById<Button>(R.id.btnCreateNewCalendar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchCalendars()

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
                calendarList.clear()
                try {
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        // અહીં સાચું કી-નામ 'calendarName' વાપર્યું છે
                        calendarList.add(CalendarModel(
                            item.getString("calendarName"),
                            "Official Community Calendar"
                        ))
                    }
                    recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                        // ભાષા પસંદગી પર જવા માટે
                        val intent = Intent(this, LanguageSelectionActivity::class.java)
                        intent.putExtra("selected_calendar", selected.name)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "ડેટા પ્રોસેસિંગમાં ભૂલ", Toast.LENGTH_SHORT).show()
                }
            },
            {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "લિસ્ટ લોડ કરવામાં એરર આવી", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }
}
