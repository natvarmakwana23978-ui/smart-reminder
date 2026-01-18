package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.indian.calendar.model.CalendarDayData

class CalendarDayAdapter(
    private var days: List<CalendarDayData>,
    private val onDayClick: (CalendarDayData) -> Unit
) : RecyclerView.Adapter<CalendarDayAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtTithi: TextView = view.findViewById(R.id.txtTithi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val day = days[position]
        
        // ENGLISH તારીખમાંથી માત્ર દિવસ (દા.ત. 15) બતાવવા માટે
        val dateParts = day.Date.split("/")
        holder.txtDate.text = if (dateParts.isNotEmpty()) dateParts[0] else ""
        
        // તમારી નવી ૨૬ ભાષાવાળી સિસ્ટમ મુજબની વિગત (Gujarati વેરીએબલનો ઉપયોગ)
        holder.txtTithi.text = day.Gujarati
        
        holder.itemView.setOnClickListener { onDayClick(day) }
    }

    override fun getItemCount() = days.size

    fun updateData(newDays: List<CalendarDayData>) {
        this.days = newDays
        notifyDataSetChanged()
    }
}
