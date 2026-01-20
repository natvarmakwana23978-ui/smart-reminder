package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        progressBar = findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadCalendars()
    }

    private fun loadCalendars() {
        progressBar.visibility = View.VISIBLE
        
        // ApiService માં કરેલા ફેરફાર મુજબ અહીં કોઈ આર્ગ્યુમેન્ટ આપવાની જરૂર નથી [cite: 2026-01-14]
        RetrofitClient.api.getCalendars().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    if (list.isNotEmpty()) {
                        // અહીં 'item: CalendarItem' લખવાથી કોમ્પાઈલર એરર દૂર થઈ જશે [cite: 2026-01-20]
                        recyclerView.adapter = CalendarAdapter(list) { item: CalendarItem ->
                            val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                            // null-safe ચેક સાથે ID મોકલવો [cite: 2026-01-20]
                            intent.putExtra("COL_INDEX", item.id?.toIntOrNull() ?: 1)
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this@CalendarSelectionActivity, "લિસ્ટ ખાલી છે", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "સર્વર ભૂલ: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "કનેક્શન નિષ્ફળ: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
