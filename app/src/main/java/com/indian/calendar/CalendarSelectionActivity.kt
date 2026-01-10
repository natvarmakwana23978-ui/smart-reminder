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
    
    // તમારી નવી અને સાચી URL અહીં ઉમેરી દીધી છે
    private val webAppUrl = "https://script.google.com/macros/s/AKfycbxoURGUvXz4bVzQrfK5i4Db4BnJK35ab7vjYyNPhEBjDxBjGJg5afbR8MUQ3WMgXqQYOw/exec"

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
        
        val request = JsonArrayRequest(Request.Method.GET, webAppUrl, null,
            { response ->
                progressBar.visibility = View.GONE
                calendarList.clear()
                try {
                    for (i in 0 until response.length()) {
                        val item = response.getJSONObject(i)
                        calendarList.add(CalendarModel(
                            item.getString("calendarName"), 
                            "Official"
                        ))
                    }
                    recyclerView.adapter = CalendarSelectionAdapter(calendarList) { selected ->
                        // જ્યારે કોઈ કેલેન્ડર પસંદ થાય ત્યારે ભાષા પસંદગી પર જાઓ
                        val intent = Intent(this@CalendarSelectionActivity, LanguageSelectionActivity::class.java)
                        intent.putExtra("selected_calendar", selected.name)
                        startActivity(intent)
                    }
                } catch (e: Exception) {
                    Toast.makeText(this@CalendarSelectionActivity, "ડેટા લોડ કરવામાં ભૂલ", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "નેટવર્ક એરર: ફરી પ્રયત્ન કરો", Toast.LENGTH_LONG).show()
            }
        )
        
        // સ્લો નેટવર્ક માટે ૨૦ સેકન્ડનો સમય આપ્યો છે
        request.retryPolicy = com.android.volley.DefaultRetryPolicy(
            20000,
            2,
            com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        Volley.newRequestQueue(this).add(request)
    }
}
