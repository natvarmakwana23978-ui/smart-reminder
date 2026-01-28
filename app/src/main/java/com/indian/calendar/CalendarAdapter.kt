package com.indian.calendar

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val items: List<Any>, private val selectedLang: String) : 
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var saturdayCounter = 0
    private var lastMonth = ""

    override fun getItemViewType(position: Int): Int = if (items[position] is String) 0 else 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val v = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
            HeaderViewHolder(v)
        } else {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
            DayViewHolder(v)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            val title = items[position] as String
            val tv = holder.itemView.findViewById<TextView>(android.R.id.text1)
            tv.text = if (title == "EMPTY") "" else title
            tv.gravity = Gravity.CENTER
            tv.setBackgroundColor(Color.parseColor("#EEEEEE"))
            tv.setTextColor(Color.BLACK)
            tv.setTypeface(null, Typeface.BOLD)
        } else if (holder is DayViewHolder) {
            val item = items[position]
            if (item == "EMPTY") {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT)
                holder.tvDate.text = ""
                holder.tvLocal.text = ""
                return
            }
            
            val day = item as CalendarDayData
            holder.tvDate.text = day.englishDate.substringBefore("/")
            val localInfo = day.allData.get(selectedLang)?.asString ?: ""
            holder.tvLocal.text = localInfo

            // ૧. શનિવાર અને રવિવાર નક્કી કરો
            val isSunday = position % 7 == 0
            val isSaturday = position % 7 == 6
            
            // ૨. મહિના મુજબ શનિવાર ગણવાનું લોજિક
            val currentMonth = day.englishDate.split("/")[1]
            if (currentMonth != lastMonth) {
                lastMonth = currentMonth
                saturdayCounter = 0
            }
            if (isSaturday) saturdayCounter++

            // ૩. બીજો અને ચોથો શનિવાર ચેક કરો
            val isSecondOrFourthSat = isSaturday && (saturdayCounter == 2 || saturdayCounter == 4)

            // ૪. તહેવાર નક્કી કરવા માટેનું લોજિક (જો લખાણ ૧૨ અક્ષરથી લાંબુ હોય તો જ તહેવાર ગણવો)
            val isFestival = localInfo.length > 12 && !localInfo.startsWith("પોષ સુદ") && !localInfo.startsWith("મહા સુદ")

            when {
                // રવિવાર અથવા બીજો/ચોથો શનિવાર: લાલ રંગ
                isSunday || isSecondOrFourthSat -> {
                    holder.itemView.setBackgroundColor(Color.RED)
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                // તહેવાર હોય ત્યારે જ: કેસરી રંગ
                isFestival -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00"))
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                // બાકીના સામાન્ય દિવસો: સફેદ બેકગ્રાઉન્ડ
                else -> {
                    holder.itemView.setBackgroundColor(Color.WHITE)
                    holder.tvDate.setTextColor(Color.BLACK)
                    holder.tvLocal.setTextColor(Color.GRAY)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
    class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }
}
