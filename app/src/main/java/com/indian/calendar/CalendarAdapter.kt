package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class CalendarAdapter(
    private val items: List<JsonObject>, // શીટના દરેક રો (Row) નો ડેટા
    private val selectedLang: String    // યુઝરે પસંદ કરેલી ભાષા
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {

    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val dayData = items[position]

        // તમારી શીટની કોલમ મુજબના નામ (દા.ત. "date", "color_code")
        val engDate = dayData.get("date")?.asString ?: ""
        val colorCode = dayData.get("color_code")?.asInt ?: 0
        
        holder.tvEnglishDate.text = engDate

        // ભાષા મુજબ તિથિ સેટ કરો
        val tithiInfo = dayData.get(selectedLang)?.asString ?: ""
        holder.tvTithi.text = tithiInfo

        // રજા માટે લાલ રંગ (color_code == 1)
        if (colorCode == 1) {
            holder.tvEnglishDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvEnglishDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.DKGRAY)
        }
    }

    override fun getItemCount(): Int = items.size
}
