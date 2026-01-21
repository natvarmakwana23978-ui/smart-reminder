package com.indian.calendar
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_selection)
        val recyclerView = findViewById<RecyclerView>(R.id.calendarSelectionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        RetrofitClient.api.getCalendarList().enqueue(object : Callback<List<CalendarItem>> {
            override fun onResponse(call: Call<List<CalendarItem>>, response: Response<List<CalendarItem>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = CalendarSelectionAdapter(response.body() ?: emptyList()) { item ->
                        val intent = Intent(this@CalendarSelectionActivity, CalendarViewActivity::class.java)
                        intent.putExtra("COL_INDEX", item.id?.toInt() ?: 1)
                        startActivity(intent)
                    }
                }
            }
            override fun onFailure(call: Call<List<CalendarItem>>, t: Throwable) {}
        })
    }
}
