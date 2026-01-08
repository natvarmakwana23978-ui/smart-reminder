package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val daysList: List<String>) : 
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    // વ્યૂ હોલ્ડર: જે આપણી ડિઝાઇનના એલિમેન્ટ્સને પકડી રાખશે
    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDate: TextView = itemView.findViewById(R.id.txtDateNumber)
        val txtCustomDay: TextView = itemView.findViewById(R.id.txtCustomDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.calendar_day_item, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val dayData = daysList[position]
        
        // અહીં આપણે ડેટાને સેટ કરીએ છીએ
        // હાલ પૂરતું આપણે પોઝિશન મુજબ તારીખ બતાવીશું
        holder.txtDate.text = (position + 1).toString()
        holder.txtCustomDay.text = dayData // આ ગૂગલ શીટમાંથી આવેલો તમારો કસ્ટમ ડેટા હશે
    }

    override fun getItemCount(): Int {
        return daysList.size
    }
}

