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

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when {
            item is String && !item.contains("Header_Day") && item != "EMPTY_SLOT" -> 0
            item is String && item.contains("Header_Day") -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> HeaderViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
            1 -> WeekdayViewHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false))
            else -> DayViewHolder(inflater.inflate(R.layout.item_calendar_day, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (holder is HeaderViewHolder) {
            val tv = holder.itemView.findViewById<TextView>(android.R.id.text1)
            tv.text = item as String
            tv.gravity = Gravity.CENTER
            tv.setBackgroundColor(Color.LTGRAY)
            tv.setTypeface(null, Typeface.BOLD)
        } else if (holder is WeekdayViewHolder) {
            val tv = holder.itemView.findViewById<TextView>(android.R.id.text1)
            val day = (item as String).replace("Header_Day_", "")
            tv.text = day
            tv.gravity = Gravity.CENTER
            tv.setTextColor(if (position % 7 == 0) Color.RED else Color.BLACK)
        } else if (holder is DayViewHolder) {
            if (item == "EMPTY_SLOT") {
                holder.itemView.visibility = View.INVISIBLE
                return
            }
            holder.itemView.visibility = View.VISIBLE
            val day = item as CalendarDayData
            val festival = day.allData.get("Name of Festival")?.asString ?: ""
            val dayInSheet = day.allData.get("Day")?.asString ?: ""
            val dateNum = day.englishDate.split("/")[0]

            holder.tvDate.text = dateNum
            holder.tvLocal.text = if (festival.isNotEmpty()) festival else (day.allData.get(selectedLang)?.asString ?: "")

            val isSun = dayInSheet.contains("Sun")
            val isSat = dayInSheet.contains("Sat")
            val d = dateNum.toInt()
            val isRedSat = isSat && ((d in 8..14) || (d in 22..28))

            when {
                isSun || isRedSat -> {
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

    override fun getItemCount() = items.size
    class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class WeekdayViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }
}
