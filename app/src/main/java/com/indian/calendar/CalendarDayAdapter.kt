package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(private val dayList: List<CalendarDayData>) :
    RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtEnglishDate: TextView = view.findViewById(R.id.txtEnglishDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
        val txtFestival: TextView = view.findViewById(R.id.txtFestival)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = dayList[position]
        
        // અંગ્રેજી તારીખમાંથી માત્ર અંક કાઢવો (દા.ત. 1/1/2026 માંથી 1)
        val dateOnly = day.Date.split("/").getOrNull(1) ?: day.Date
        holder.txtEnglishDate.text = dateOnly

        // તિથિ અને તહેવાર સેટ કરવા
        holder.txtTithi.text = day.Tithi
        holder.txtFestival.text = day.Festival_English

        // જો રવિવાર હોય તો લાલ રંગ (તમારી શીટમાં Day મુજબ સેટ કરી શકાય)
        if (day.Day.contains("Sun", ignoreCase = true)) {
            holder.txtEnglishDate.textColor = Color.RED
        } else {
            holder.txtEnglishDate.textColor = Color.BLACK
        }
    }

    override fun getItemCount(): Int = dayList.size
}
