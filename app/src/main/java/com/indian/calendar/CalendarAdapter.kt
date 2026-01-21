package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvEnglish: TextView = view.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = view.findViewById(R.id.tvLocalDate)
        val tvAlert: TextView = view.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // જો તારીખ "21-1-2026" હોય તો તેમાંથી માત્ર "21" લેવા માટે [cite: 2026-01-21]
        val dateParts = day.englishDate?.split("-")
        holder.tvEnglish.text = if (dateParts != null && dateParts.isNotEmpty()) dateParts[0] else ""
        
        holder.tvLocal.text = day.localDate ?: ""

        // તહેવારની લાલ પટ્ટી [cite: 2026-01-21]
        if (!day.alert.isNullOrEmpty()) {
            holder.tvAlert.visibility = View.VISIBLE
            holder.tvAlert.text = day.alert
        } else {
            holder.tvAlert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
