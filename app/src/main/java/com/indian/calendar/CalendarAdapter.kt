package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.indian.calendar.R

class CalendarAdapter(
    private val days: List<CalendarDayData>,
    private val selectedHeader: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        // ખાતરી કરો કે item_calendar_day.xml માં આ જ ID છે
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // ૧. તારીખ છૂટી પાડવી (1/1/2026 માંથી માત્ર '1' લેવા માટે)
        val fullDate = day.englishDate // "1/1/2026"
        val dateParts = fullDate.split("/")
        val displayDate = if (dateParts.isNotEmpty()) dateParts[0] else ""
        
        holder.tvEng.text = displayDate

        // ૨. ગૂગલ શીટમાં જે હેડર સિલેક્ટ કર્યું હોય (દા.ત. ગુજરાતી (Gujarati)) તેનો ડેટા
        val calendarInfo = day.allData.get(selectedHeader)?.asString ?: ""
        
        // ટૂંકું લખાણ બતાવવું (દા.ત. પોષ સુદ-૧૩)
        holder.tvLoc.text = calendarInfo
        
        // ૩. તમારો કલર કોડ પ્લાન
        // તમારી શીટમાં છેલ્લી કોલમમાં 'Sunday' કે 'Holiday' લખેલું હશે તો તે મુજબ રંગ બદલાશે
        val rawData = day.allData.toString() // આખા ડેટામાં ક્યાંય શબ્દ છે કે નહીં તે ચેક કરવા
        
        when {
            rawData.contains("Sunday", ignoreCase = true) || rawData.contains("Holiday", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // લાલ
                holder.tvEng.setTextColor(Color.RED)
            }
            rawData.contains("નૂતન વર્ષ", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0")) // કેસરી
            }
            else -> {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.tvEng.setTextColor(Color.BLACK)
            }
        }

        // ૪. ક્લિક કરવાથી પોપઅપ કાર્ડ ખુલે
        holder.itemView.setOnClickListener {
            showDetailsCard(holder.itemView.context, day, calendarInfo)
        }
    }

    private fun showDetailsCard(context: android.content.Context, day: CalendarDayData, info: String) {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_day_details, null)
        
        view.findViewById<TextView>(R.id.tvDetailTitle).text = "તારીખ: ${day.englishDate}"
        view.findViewById<TextView>(R.id.tvDetailInfo).text = info
        
        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.setContentView(view)
        dialog.show()
    }

    override fun getItemCount() = days.size
}
