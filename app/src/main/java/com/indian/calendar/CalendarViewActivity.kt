package com.indian.calendar

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
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
        
        // ઇન્ટેન્ટમાંથી ઇન્ડેક્સ મેળવો અથવા ડિફોલ્ટ ૧
        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        loadData(colIndex)
    }

    private fun setupMonthButtons() {
        for (i in 1..12) {
            val btn = Button(this).apply {
                text = i.toString()
                setOnClickListener { 
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset((i - 1) * 31, 0) 
                }
            }
            monthSelectionLayout.addView(btn)
        }
    }

    private fun loadData(colIndex: Int) {
        // અહીં સુધારો કર્યો છે: 'action' અને 'colIndex' બંને નામ સાથે મોકલ્યા છે
        RetrofitClient.api.getCalendarData(action = "getCalendarDays", colIndex = colIndex)
            .enqueue(object : Callback<List<CalendarDayData>> {
                override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                    if (response.isSuccessful) {
                        recyclerView.adapter = CalendarDayAdapter(response.body() ?: emptyList(), colIndex)
                    } else {
                        Toast.makeText(this@CalendarViewActivity, "ડેટા નથી મળ્યો", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                    Toast.makeText(this@CalendarViewActivity, "નેટવર્ક એરર: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
