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
        val tvEnglishDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocalDate: TextView = v.findViewById(R.id.tvLocalDate)
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

            // ૧. અંગ્રેજી તારીખ મેળવવી (દા.ત. 30/01/2026 માંથી 30)
            val fullDate = item.englishDate
            val datePart = if (fullDate.contains("/")) fullDate.split("/")[0] else fullDate
            holder.tvEnglishDate.text = datePart

            // ૨. સુરક્ષિત રીતે ડેટા મેળવવો (Null અને Emoji Safe)
            // પસંદ કરેલી ભાષા મુજબનો ડેટા (તિથિ વગેરે)
            val localText = data.get(selectedLang)?.let { 
                if (it.isJsonNull) "" else it.asString 
            } ?: ""

            // યુઝરની પર્સનલ નોટ (સ્માર્ટ રીમાઇન્ડર)
            val userNote = data.get("Note")?.let { 
                if (it.isJsonNull) "" else it.asString 
            } ?: ""

            // તહેવારનું નામ
            val festival = data.get("Name of Festival")?.let { 
                if (it.isJsonNull) "" else it.asString 
            } ?: ""

            // ૩. ડિસ્પ્લે લોજિક: જો યુઝરની પોતાની નોટ (Note) હોય તો તે પહેલા બતાવો
            if (userNote.isNotEmpty()) {
                holder.tvLocalDate.text = userNote
                holder.tvLocalDate.setTextColor(Color.BLUE) // રીમાઇન્ડર માટે બ્લુ કલર
                holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD")) // આછો બ્લુ બેકગ્રાઉન્ડ
            } else if (festival.isNotEmpty()) {
                holder.tvLocalDate.text = festival
                holder.tvLocalDate.setTextColor(Color.RED)
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0")) // આછો નારંગી
            } else {
                holder.tvLocalDate.text = localText
                holder.tvLocalDate.setTextColor(Color.DKGRAY)
                holder.itemView.setBackgroundColor(Color.WHITE)
            }

            // ૪. રવિવાર માટે લાલ તારીખ
            val dayName = data.get("Day")?.let { if (it.isJsonNull) "" else it.asString } ?: ""
            if (dayName.contains("Sun", ignoreCase = true)) {
                holder.tvEnglishDate.setTextColor(Color.RED)
            } else {
                holder.tvEnglishDate.setTextColor(Color.BLACK)
            }

        } catch (e: Exception) {
            // જો ડેટામાં ઇમોજી કે કોઈ એરર હોય તો એપ ક્રેસ થવાને બદલે ખાલી ખાનું બતાવશે
            holder.tvEnglishDate.text = ""
            holder.tvLocalDate.text = ""
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int = items.size
}
