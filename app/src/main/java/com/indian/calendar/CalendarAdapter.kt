package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(
    private val days: List<CalendarDayData?>,
    private val selectedHeader: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        if (day == null) {
            holder.tvDate.text = ""
            holder.tvLocal.text = ""
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            return
        }

        holder.tvDate.text = day.englishDate.split("/")[0]
        val info = day.allData.get(selectedHeader)?.asString ?: ""
        holder.tvLocal.text = info

        // કલર કોડિંગ લોજિક
        val raw = day.allData.toString()
        when {
            raw.contains("Sunday") || raw.contains("Holiday") -> {
                holder.tvDate.setTextColor(Color.RED) // શનિ-રવિ/રજા
            }
            raw.contains("સુદ") || raw.contains("વદ") -> {
                holder.tvDate.setTextColor(Color.parseColor("#FF9800")) // હિન્દુ (કેસરી)
            }
            raw.contains("Rajab") || selectedHeader.contains("Islamic") -> {
                holder.tvDate.setTextColor(Color.parseColor("#4CAF50")) // મુસ્લિમ (લીલો)
            }
            raw.contains("New Year") -> {
                holder.tvDate.setTextColor(Color.BLUE) // ઇસાઈ (બ્લુ)
            }
            else -> holder.tvDate.setTextColor(Color.BLACK)
        }
    }

    override fun getItemCount() = days.size
}
