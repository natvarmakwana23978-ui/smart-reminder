package com.indian.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class CalendarAdapter(
    private val items: List<CalendarDayData>, 
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocalDate: TextView = v.findViewById(R.id.tvLocalDate)
        val tvMonthStart: TextView = v.findViewById(R.id.tvMonthStart) // નવું ID
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        try {
            val item = items[position]
            val data = item.allData

            // ૧. અંગ્રેજી તારીખ (વચોવચ - મોટી)
            val fullDate = item.englishDate
            holder.tvEnglishDate.text = if (fullDate.contains("/")) fullDate.split("/")[0] else fullDate

            // ૨. નવો મહિનો શરૂ થવાની સૂચના (દા.ત. મહા માસ શરૂ)
            val monthStart = data.get("MonthStart")?.let { if (it.isJsonNull) "" else it.asString } ?: ""
            if (monthStart.isNotEmpty()) {
                holder.tvMonthStart.visibility = View.VISIBLE
                holder.tvMonthStart.text = monthStart
                holder.tvMonthStart.setTextColor(Color.parseColor("#E91E63")) // Pink/Red color for highlight
            } else {
                holder.tvMonthStart.visibility = View.GONE
            }

            // ૩. લોકલ તિથિ અને તહેવાર (નીચે)
            val localTithi = data.get(selectedLang)?.let { if (it.isJsonNull) "" else it.asString } ?: ""
            val festival = data.get("Name of Festival")?.let { if (it.isJsonNull) "" else it.asString } ?: ""
            val userNote = data.get("Note")?.let { if (it.isJsonNull) "" else it.asString } ?: ""

            // જો યુઝરની નોટ હોય તો તેને પ્રાધાન્ય આપવું
            holder.tvLocalDate.text = when {
                userNote.isNotEmpty() -> userNote
                festival.isNotEmpty() -> festival
                else -> localTithi
            }

            // રંગોની ગોઠવણ
            if (userNote.isNotEmpty()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E1F5FE"))
            } else if (festival.isNotEmpty()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0"))
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = items.size
}
