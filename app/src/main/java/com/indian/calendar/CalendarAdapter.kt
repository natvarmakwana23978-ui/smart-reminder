package com.indian.calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val eng: TextView = v.findViewById(R.id.tvEnglishDate)
        val loc: TextView = v.findViewById(R.id.tvLocalDate)
        val alert: TextView = v.findViewById(R.id.tvAlert)
    }
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_calendar_day, p, false))
    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val d = days[pos]
        h.eng.text = d.englishDate?.split("-")?.get(0) ?: ""
        h.loc.text = d.localDate ?: ""
        h.alert.visibility = if (d.alert.isNullOrEmpty()) View.GONE else View.VISIBLE
        h.alert.text = d.alert
    }
    override fun getItemCount() = days.size
}
