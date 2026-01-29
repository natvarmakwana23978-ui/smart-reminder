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
            if (title == "EMPTY_SLOT") {
                tv.text = ""
                tv.setBackgroundColor(Color.TRANSPARENT)
            } else {
                tv.text = title
                tv.gravity = Gravity.CENTER
                tv.setBackgroundColor(Color.parseColor("#F5F5F5"))
                tv.setTextColor(Color.BLACK)
                tv.setTypeface(null, Typeface.BOLD)
                tv.setPadding(0, 20, 0, 20)
            }
        } else if (holder is DayViewHolder) {
            val item = items[position]
            if (item == "EMPTY_SLOT") {
                holder.itemView.visibility = View.INVISIBLE
                return
            }
            holder.itemView.visibility = View.VISIBLE
            val day = item as CalendarDayData
            val dateNum = day.englishDate.split("/")[0]
            holder.tvDate.text = dateNum
            
            val localInfo = day.allData.get(selectedLang)?.asString ?: ""
            holder.tvLocal.text = localInfo

            val dayName = day.allData.get("Day")?.asString ?: ""
            val festival = day.allData.get("Name of Festival")?.asString ?: ""

            val isSunday = dayName.contains("Sun")
            val isSaturday = dayName.contains("Sat")
            val d = dateNum.toInt()
            val isRedSat = isSaturday && ((d in 8..14) || (d in 22..28))

            when {
                isSunday || isRedSat -> {
                    holder.itemView.setBackgroundColor(Color.RED)
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
                festival.isNotEmpty() -> {
                    holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00"))
                    holder.tvDate.setTextColor(Color.WHITE)
                    holder.tvLocal.setTextColor(Color.WHITE)
                }
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
