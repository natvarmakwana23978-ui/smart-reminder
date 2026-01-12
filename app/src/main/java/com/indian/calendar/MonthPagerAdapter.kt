package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MonthPagerAdapter(private val startCalendar: Calendar) : 
    RecyclerView.Adapter<MonthPagerAdapter.MonthViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.month_item, parent, false)
        return MonthViewHolder(view)
    }

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        val monthCalendar = startCalendar.clone() as Calendar
        monthCalendar.add(Calendar.MONTH, position - 500)
        
        val daysInMonth = getDaysInMonthArray(monthCalendar)
        val calendarAdapter = CalendarAdapter(daysInMonth)
        
        holder.recyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.recyclerView.adapter = calendarAdapter
    }

    override fun getItemCount(): Int = 1000 

    private fun getDaysInMonthArray(calendar: Calendar): List<String> {
        val daysList = mutableListOf<String>()
        val monthCalendar = calendar.clone() as Calendar
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        val firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1
        val daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        for (i in 0 until firstDayOfWeek) { daysList.add("") }
        for (i in 1..daysInMonth) { daysList.add(i.toString()) }
        return daysList
    }

    class MonthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.monthRecyclerView)
    }
}
