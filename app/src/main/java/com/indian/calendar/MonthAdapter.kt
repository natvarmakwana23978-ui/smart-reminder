package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MonthAdapter(private val months: Map<String, List<CalendarDayData>>) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private val monthKeys = months.keys.toList()

    class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonthHeader: TextView = view.findViewById(R.id.tvMonthYearLabel)
        val rvDays: RecyclerView = view.findViewById(R.id.calendarRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_month_container, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthName = monthKeys[position]
        val daysInMonth = months[monthName] ?: emptyList()
        
        // હેડર સેટ કરો (દા.ત. Jan 2026) [cite: 2026-01-21]
        val year = daysInMonth[0].englishDate?.split(" ")?.get(3) ?: ""
        holder.tvMonthHeader.text = "$monthName $year"

        // ૧ તારીખનો વાર શોધીને ખાલી ખાના (Offset) ગણવા [cite: 2026-01-21]
        val firstDayName = daysInMonth[0].englishDate?.split(" ")?.get(0) ?: "Sun"
        val offset = when(firstDayName) {
            "Sun" -> 0; "Mon" -> 1; "Tue" -> 2; "Wed" -> 3; "Thu" -> 4; "Fri" -> 5; "Sat" -> 6; else -> 0
        }

        // ગ્રીડ માટે ડેટા તૈયાર કરો [cite: 2026-01-21]
        val displayDays = mutableListOf<CalendarDayData>()
        for (i in 1..offset) displayDays.add(CalendarDayData("", "", "")) // ખાલી ખાના [cite: 2026-01-21]
        displayDays.addAll(daysInMonth)

        holder.rvDays.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.rvDays.adapter = CalendarAdapter(displayDays)
    }

    override fun getItemCount() = monthKeys.size
}
