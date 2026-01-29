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
            item is String && item.startsWith("HEADER|") -> 0
            item is String && item.startsWith("Header_Day_") -> 1
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
            tv.text = (item as String).replace("HEADER|", "")
            tv.gravity = Gravity.CENTER
            tv.textSize = 20f
            tv.setTypeface(null, Typeface.BOLD)
            tv.setPadding(0, 20, 0, 20)
        } else if (holder is WeekdayViewHolder) {
            val tv = holder.itemView.findViewById<TextView>(android.R.id.text1)
            tv.text = (item as String).replace("Header_Day_", "")
            tv.gravity = Gravity.CENTER
            tv.textSize = 12f
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

            // ૧. વાર (ઉપર)
            holder.tvDayLabel?.text = dayInSheet
            // ૨. અંગ્રેજી તારીખ (મોટી વચ્ચે)
            holder.tvDate.text = dateNum
            // ૩. તિથિ/તહેવાર (નીચે)
            holder.tvLocal.text = if (festival.isNotEmpty()) festival else (day.allData.get(selectedLang)?.asString ?: "")

            val isSun = dayInSheet.contains("Sun")
            val isRedSat = dayInSheet.contains("Sat") && ((dateNum.toInt() in 8..14) || (dateNum.toInt() in 22..28))

            if (isSun || isRedSat) {
                holder.itemView.setBackgroundColor(Color.RED)
                setWhiteText(holder)
            } else if (festival.isNotEmpty()) {
                holder.itemView.setBackgroundColor(Color.parseColor("#FF8C00"))
                setWhiteText(holder)
            } else {
                holder.itemView.setBackgroundColor(Color.WHITE)
                holder.tvDate.setTextColor(Color.BLACK)
                holder.tvDayLabel?.setTextColor(Color.GRAY)
                holder.tvLocal.setTextColor(Color.DKGRAY)
            }
        }
    }

    private fun setWhiteText(h: DayViewHolder) {
        h.tvDate.setTextColor(Color.WHITE)
        h.tvDayLabel?.setTextColor(Color.WHITE)
        h.tvLocal.setTextColor(Color.WHITE)
    }

    override fun getItemCount() = items.size
    class HeaderViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class WeekdayViewHolder(v: View) : RecyclerView.ViewHolder(v)
    class DayViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvDayLabel: TextView? = v.findViewById(R.id.tvDayLabel)
        val tvDate: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLocal: TextView = v.findViewById(R.id.tvLocalDate)
    }
}
