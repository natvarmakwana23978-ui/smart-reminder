package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val list: List<CalendarModel>,
    private val onClick: (CalendarModel) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // અહીં ID ચેક કરો, તે XML માં જે છે તે જ હોવા જોઈએ
        val txtCalendarName: TextView = view.findViewById(R.id.txtCalendarName)
        val txtCreatorName: TextView = view.findViewById(R.id.txtCreatorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txtCalendarName.text = item.name
        holder.txtCreatorName.text = item.creator
        
        holder.itemView.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount() = list.size
}
