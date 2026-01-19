package com.indian.calendar
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.indian.calendar.model.CalendarDayData
import retrofit2.*

class CalendarViewActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var monthSelectionLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        recyclerView = findViewById(R.id.calendarRecyclerView)
        monthSelectionLayout = findViewById(R.id.monthSelectionLayout)
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupMonthButtons()
        loadData(intent.getIntExtra("COL_INDEX", 1))
    }

    private fun setupMonthButtons() {
        for (i in 1..12) {
            val btn = Button(this).apply {
                text = i.toString()
                setOnClickListener { (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset((i - 1) * 31, 0) }
            }
            monthSelectionLayout.addView(btn)
        }
    }

    private fun loadData(colIndex: Int) {
        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                if (response.isSuccessful) {
                    recyclerView.adapter = CalendarDayAdapter(response.body() ?: emptyList(), colIndex)
                }
            }
            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {}
        })
    }
}
