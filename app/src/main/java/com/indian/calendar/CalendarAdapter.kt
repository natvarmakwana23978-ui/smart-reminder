package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val daysList: List<String>,
    private val sheetDataMap: Map<String, CalendarDayData>,
    private val monthYearStr: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDay: TextView = view.findViewById(R.id.tvDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = daysList[position]
        
        if (day.isNotEmpty()) {
            val dateKey = "${monthYearStr.split("/")[0]}/$day/${monthYearStr.split("/")[1]}"
            val data = sheetDataMap[dateKey]
            
            // તારીખ અને તેની નીચે નાની તિથિ બતાવવા માટે
            val displayInfo = if (data?.detail != null) "$day\n${data.detail}" else day
            holder.tvDay.text = displayInfo
            
            // જો તહેવાર હોય તો રંગ બદલવો
            if (!data?.festival.isNullOrEmpty()) {
                holder.tvDay.setTextColor(Color.RED)
            } else {
                holder.tvDay.setTextColor(Color.BLACK)
            }
        } else {
            holder.tvDay.text = ""
        }
    }

    override fun getItemCount() = daysList.size
}
