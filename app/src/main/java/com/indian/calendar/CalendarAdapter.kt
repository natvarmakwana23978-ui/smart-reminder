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
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ખાતરી કરો કે item_calendar_day.xml માં tvEnglishDate અને tvLocalDate ID છે
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // જો તારીખ ખાલી હોય તો ખાનું છુપાવો
        if (day.englishDate.isEmpty()) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }
        holder.itemView.visibility = View.VISIBLE

        // ૧. અંગ્રેજી તારીખ બતાવો (દા.ત. 01, 02)
        val dateParts = day.englishDate.split("/")
        holder.tvEng.text = if (dateParts.isNotEmpty()) dateParts[0] else ""

        // ૨. ગૂગલ શીટની પસંદ કરેલી કોલમનો ડેટા (તહેવાર/તિથિ)
        val calendarInfo = day.allData.get(selectedHeader)?.asString ?: ""
        holder.tvLoc.text = calendarInfo

        // ૩. તમારા આઈડિયા મુજબ કલર કોડિંગ [cite: 2026-01-23]
        // નોંધ: શીટમાં 'Category' નામની કોલમ હોવી જોઈએ
        val category = day.allData.get("Category")?.asString ?: ""
        
        when {
            // રવિવાર અને બેંક રજા માટે લાલ [cite: 2026-01-23]
            category.contains("Holiday", ignoreCase = true) || category.contains("Sunday", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // આછો લાલ
                holder.tvEng.setTextColor(Color.RED)
            }
            // હિન્દુ તહેવાર માટે કેસરી [cite: 2026-01-23]
            category.contains("Hindu", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFF3E0")) // આછો કેસરી
            }
            // મુસ્લિમ તહેવાર માટે લીલો [cite: 2026-01-23]
            category.contains("Muslim", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#E8F5E9")) // આછો લીલો
            }
            // ઈસાઈ તહેવાર માટે વાદળી [cite: 2026-01-23]
            category.contains("Christian", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#E3F2FD")) // આછો વાદળી
            }
            // કસ્ટમ રિમાઇન્ડર માટે ગુલાબી [cite: 2026-01-23]
            category.contains("Personal", ignoreCase = true) || category.contains("Birthday", ignoreCase = true) -> {
                holder.itemView.setBackgroundColor(Color.parseColor("#FCE4EC")) // આછો ગુલાબી
            }
            else -> {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.tvEng.setTextColor(Color.BLACK)
            }
        }

        // ૪. તારીખ પર ટચ કરવાથી કાર્ડ (Bottom Sheet) ખોલો [cite: 2026-01-23]
        holder.itemView.setOnClickListener {
            showDetailsCard(holder.itemView.context, day, calendarInfo)
        }
    }

    private fun showDetailsCard(context: android.content.Context, day: CalendarDayData, info: String) {
        val dialog = BottomSheetDialog(context)
        // આપણે હમણાં બનાવેલું layout_day_details.xml લેઆઉટ [cite: 2026-01-23]
        val view = LayoutInflater.from(context).inflate(R.layout.layout_day_details, null)
        
        view.findViewById<TextView>(R.id.tvDetailTitle).text = "તારીખ: ${day.englishDate}"
        view.findViewById<TextView>(R.id.tvDetailInfo).text = info
        
        // બંધ કરો બટન
        view.findViewById<Button>(R.id.btnClose).setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.setContentView(view)
        dialog.show()
    }

    override fun getItemCount() = days.size
}
