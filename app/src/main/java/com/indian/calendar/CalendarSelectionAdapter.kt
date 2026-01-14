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

    // અહીં આપણે ViewHolder માં TextView ને બરાબર સેટ કરીશું
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // આપણે એન્ડ્રોઇડનું ડિફોલ્ટ લેઆઉટ વાપરી રહ્યા છીએ
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        
        // જો item.name ખાલી હોય તો "No Name" બતાવશે (ટેસ્ટિંગ માટે)
        holder.textView.text = if (item.name.isNotEmpty()) item.name else "Unnamed Calendar"
        
        holder.itemView.setOnClickListener { 
            onItemClick(item, position) 
        }
    }

    override fun getItemCount() = list.size
}
