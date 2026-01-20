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
        try {
            setContentView(R.layout.activity_calendar_selection)
            recyclerView = findViewById(R.id.calendarRecyclerView)
            progressBar = findViewById(R.id.progressBar)
            recyclerView.layoutManager = LinearLayoutManager(this)
            loadCalendars()
        } catch (e: Exception) {
            Toast.makeText(this, "ડિઝાઇન લોડ કરવામાં ભૂલ: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadCalendars() {
        progressBar.visibility = View.VISIBLE
        RetrofitClient.api.getCalendars().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    recyclerView.adapter = CalendarSelectionAdapter(list) { item ->
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("COL_INDEX", item.id?.toIntOrNull() ?: 1)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this@CalendarSelectionActivity, "સર્વર ભૂલ", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@CalendarSelectionActivity, "કનેક્શન નિષ્ફળ", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
