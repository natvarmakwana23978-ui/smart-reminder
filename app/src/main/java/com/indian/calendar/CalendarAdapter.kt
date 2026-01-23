package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.indian.calendar.R

class CalendarAdapter(
    private val days: List<CalendarDayData>,
    private val selectedHeader: String
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // અંગ્રેજી તારીખ સેટ કરો
        val dateOnly = day.englishDate.split("/").getOrNull(0) ?: ""
        holder.tvEng.text = dateOnly

        // પસંદ કરેલા કેલેન્ડરની વિગત (શ્રાવણ વદ 13...)
        val info = day.allData.get(selectedHeader)?.asString ?: ""
        holder.tvLoc.text = info

        // કલર કોડિંગ મુજબ બેકગ્રાઉન્ડ સેટ કરો
        when(day.colorCode) {
            1 -> holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // આછો લાલ
            2 -> holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0")) // આછો કેસરી
            3 -> holder.itemView.setBackgroundColor(Color.parseColor("#E8F5E9")) // આછો લીલો
            4 -> holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD")) // આછો વાદળી
            5 -> holder.itemView.setBackgroundColor(Color.parseColor("#FCE4EC")) // આછો ગુલાબી
            else -> holder.itemView.setBackgroundColor(Color.WHITE)
        }

        // ટચ કરવાથી કાર્ડ (BottomSheet) ખુલશે
        holder.itemView.setOnClickListener {
            showDetailsCard(holder.itemView.context, day, info)
        }
    }

    private fun showDetailsCard(context: android.content.Context, day: CalendarDayData, info: String) {
        val dialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.layout_day_details, null)
        
        view.findViewById<TextView>(R.id.tvDetailTitle).text = day.englishDate
        view.findViewById<TextView>(R.id.tvDetailInfo).text = info
        
        dialog.setContentView(view)
        dialog.show()
    }

    override fun getItemCount() = days.size
}
