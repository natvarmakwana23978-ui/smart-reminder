package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
        val tvAlert: TextView = v.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = 
        ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_calendar_day, p, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val d = days[pos]
        
        // ૧. જો ડેટા ખાલી હોય (Offset days), તો ખાનું છુપાવો [cite: 2026-01-21]
        if (d.englishDate.isNullOrEmpty()) {
            h.itemView.visibility = View.INVISIBLE
            return
        }
        
        h.itemView.visibility = View.VISIBLE
        
        // ૨. માત્ર તારીખનો આંકડો જ બતાવો [cite: 2026-01-21]
        val parts = d.englishDate.split(" ")
        h.tvEng.text = if (parts.size >= 3) parts[2] else ""
        h.tvLoc.text = d.localDate ?: ""

        // ૩. ખોટા ડેટાને ફિલ્ટર કરવાનું લોજિક (Clean-up) [cite: 2026-01-21]
        val invalidWords = listOf("Tevet", "Shevat", "Adar", "Rajab", "Shaban", "Ramadan")
        val alertText = d.alert ?: ""
        
        val isInvalid = invalidWords.any { alertText.contains(it, ignoreCase = true) }

        // ૪. જો લાલ પટ્ટીમાં સાચો તહેવાર હોય તો જ બતાવો [cite: 2026-01-21]
        if (alertText.isNotEmpty() && !isInvalid) {
            h.tvAlert.visibility = View.VISIBLE
            h.tvAlert.text = alertText
        } else {
            h.tvAlert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
