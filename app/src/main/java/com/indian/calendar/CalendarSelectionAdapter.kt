package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val list: List<CalendarModel>,
    private val onItemClick: (CalendarModel, Int) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txtCalendarName)
        val txtCreator: TextView = view.findViewById(R.id.txtCreatorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // અહીં આપણે આપણી નવી XML ફાઈલ (item_calendar_selection) વાપરી રહ્યા છીએ
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_selection, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txtName.text = item.name
        holder.txtCreator.text = item.creator
        
        holder.itemView.setOnClickListener { 
            onItemClick(item, position) 
        }
    }

    override fun getItemCount() = list.size
}
