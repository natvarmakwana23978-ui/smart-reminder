package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// આ એડપ્ટર કેલેન્ડરના લિસ્ટ (ગુજરાતી, હિન્દી વગેરે) બતાવવા માટે છે [cite: 2026-01-20]
class CalendarAdapter(
    private val list: List<CalendarItem>,
    private val onClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // એન્ડ્રોઇડનું ડિફોલ્ટ લેઆઉટ 'simple_list_item_1' વાપર્યું છે જેથી XML ની ભૂલ ન આવે [cite: 2026-01-14]
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.txtName.text = item.name ?: "Unknown Calendar"
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = list.size
}

