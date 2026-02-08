package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

// ડેટા ક્લાસ - જે શીટ માંથી ડેટા લેશે
data class CalendarDayData(
    val englishDate: String,
    val colorCode: Int, // ૧ એટલે રજા
    val allData: JsonObject, // તમારી ૨૭ કેલેન્ડરની વિગતો
    val isSecondOrFourthSaturday: Boolean = false,
    val isSunday: Boolean = false
)

class CalendarAdapter(
    private val items: List<CalendarDayData>, 
    private val selectedLang: String
) : RecyclerView.Adapter<CalendarAdapter.DayViewHolder>() {
    
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvTithi: TextView = v.findViewById(R.id.tvTithi)
        val container: View = v // આખું ખાનું
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = items[position]
        
        holder.tvEnglishDate.text = day.englishDate
        
        if (day.englishDate.isEmpty()) {
            holder.tvTithi.text = ""
            return
        }

        // તમારી પસંદ કરેલી ભાષા મુજબ તિથિ/માહિતી સેટ કરો
        val localInfo = if (day.allData.has(selectedLang)) {
            day.allData.get(selectedLang).asString
        } else {
            ""
        }
        holder.tvTithi.text = localInfo

        // --- રંગ સેટ કરવાનું લોજિક ---
        // જો colorCode ૧ હોય (રજા) અથવા રવિવાર હોય અથવા ૨જો/૪થો શનિવાર હોય તો લાલ
        if (day.colorCode == 1 || day.isSunday || day.isSecondOrFourthSaturday) {
            holder.tvEnglishDate.setTextColor(Color.RED)
            holder.tvTithi.setTextColor(Color.RED)
        } else {
            holder.tvEnglishDate.setTextColor(Color.BLACK)
            holder.tvTithi.setTextColor(Color.parseColor("#555555")) // ડાર્ક ગ્રે
        }
    }

    override fun getItemCount(): Int = items.size
}
