package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val eng: TextView = v.findViewById(R.id.tvEnglishDate)
        val loc: TextView = v.findViewById(R.id.tvLocalDate)
        val alert: TextView = v.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = 
        ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_calendar_day, p, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val d = days[pos]
        
        // "Thu Jan 01 2026 00:0" માંથી માત્ર "01" લેવા માટે [cite: 2026-01-21]
        val parts = d.englishDate?.split(" ")
        val dateOnly = if (parts != null && parts.size >= 3) parts[2] else ""
        
        h.eng.text = dateOnly
        h.loc.text = d.localDate ?: ""

        // તહેવારની લાલ પટ્ટી જો ડેટા હોય તો જ બતાવવી [cite: 2026-01-07, 2026-01-21]
        if (!d.alert.isNullOrEmpty()) {
            h.alert.visibility = View.VISIBLE
            h.alert.text = d.alert
        } else {
            h.alert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
