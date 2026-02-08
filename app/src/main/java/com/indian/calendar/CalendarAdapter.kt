package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val items: List<CalendarDayData>,
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate) // XML માં ID ચેક કરી લેવી
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = items[position]
        holder.tvDate.text = day.date
        
        // ભાષા મુજબ વિગત બતાવો
        val info = if (day.details?.has(selectedLang) == true) {
            day.details.get(selectedLang).asString
        } else { "" }
        holder.tvTithi.text = info

        // રજા હોય તો લાલ રંગ
        if (day.color_code == 1 || day.isSunday) {
            holder.tvDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.GRAY)
        }
    }

    override fun getItemCount(): Int = items.size
}
