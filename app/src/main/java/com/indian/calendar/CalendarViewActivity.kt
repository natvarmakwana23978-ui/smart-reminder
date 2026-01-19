package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var monthSelectionLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)

        val titleTxt = findViewById<TextView>(R.id.calendarTitleText)
        recyclerView = findViewById(R.id.calendarRecyclerView)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        monthSelectionLayout = findViewById(R.id.monthSelectionLayout) // આ લેઆઉટ XML માં હોવું જરૂરી છે

        val colIndex = intent.getIntExtra("COL_INDEX", 1)
        val calendarName = intent.getStringExtra("CALENDAR_NAME") ?: "કેલેન્ડર"
        titleTxt?.text = "-:: $calendarName ::-"

        // વર્ટિકલ સ્ક્રોલિંગ માટે LinearLayoutManager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // ૧ થી ૧૨ નંબરના બટન બનાવવાનું ફંક્શન
        setupMonthButtons()

        progressBar?.visibility = View.VISIBLE

        RetrofitClient.api.getCalendarData(colIndex).enqueue(object : Callback<List<CalendarDayData>> {
            override fun onResponse(call: Call<List<CalendarDayData>>, response: Response<List<CalendarDayData>>) {
                progressBar?.visibility = View.GONE
                if (response.isSuccessful) {
                    val data = response.body() ?: emptyList()
                    if (data.isNotEmpty()) {
                        // એડપ્ટરમાં ડેટા સેટ કરવો
                        recyclerView.adapter = CalendarDayAdapter(data, colIndex) { selectedDay ->
                            // ક્લિક ઇવેન્ટ
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<CalendarDayData>>, t: Throwable) {
                progressBar?.visibility = View.GONE
                Toast.makeText(this@CalendarViewActivity, "ભૂલ: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupMonthButtons() {
        monthSelectionLayout.removeAllViews()
        for (i in 1..12) {
            val button = Button(this).apply {
                text = i.toString()
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(4, 0, 4, 0) }
                
                setOnClickListener {
                    // મહિના મુજબ અંદાજિત પોઝિશન પર જમ્પ મારવો
                    val position = (i - 1) * 31 
                    (recyclerView.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
                    Toast.makeText(context, "મહિનો: $i", Toast.LENGTH_SHORT).show()
                }
            }
            monthSelectionLayout.addView(button)
        }
    }
}
