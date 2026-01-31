package com.indian.calendar

import android.graphics.Color
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
        val tvMonthStart: TextView = v.findViewById(R.id.tvMonthStart)
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvFestival: TextView = v.findViewById(R.id.tvFestival)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        try {
            val data = items[position].allData

            // ૧. ઉપર: માસ પરિવર્તન
            val monthStart = getString(data, "MonthStart")
            holder.tvMonthStart.text = monthStart
            holder.tvMonthStart.visibility = if (monthStart.isEmpty()) View.INVISIBLE else View.VISIBLE

            // ૨. મધ્ય: અંગ્રેજી તારીખ
            val fullDate = getString(data, "ENGLISH")
            holder.tvEnglishDate.text = if (fullDate.contains("/")) fullDate.split("/")[0] else fullDate

            // ૩. નીચે: તહેવાર અથવા સ્માર્ટ રીમાઇન્ડર (Note)
            val festival = getString(data, "Name of Festival")
            val note = getString(data, "Note")
            
            if (note.isNotEmpty()) {
                holder.tvFestival.text = "📌 $note"
                holder.tvFestival.setTextColor(Color.BLUE)
                holder.itemView.setBackgroundColor(Color.parseColor("#E1F5FE")) // બ્લુ બેકગ્રાઉન્ડ
            } else {
                holder.tvFestival.text = festival
                holder.tvFestival.setTextColor(Color.RED)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }

            // ૪. તળિયે: લોકલ તિથિ
            holder.tvTithi.text = getString(data, selectedLang)

            // રવિવાર માટે લાલ તારીખ
            val day = getString(data, "Day")
            if (day.contains("Sun", true)) holder.tvEnglishDate.setTextColor(Color.RED)
            else holder.tvEnglishDate.setTextColor(Color.BLACK)

        } catch (e: Exception) { e.printStackTrace() }
    }

    private fun getString(obj: JsonObject, key: String): String {
        return obj.get(key)?.let { if (it.isJsonNull) "" else it.asString } ?: ""
    }

    override fun getItemCount(): Int = items.size
}
