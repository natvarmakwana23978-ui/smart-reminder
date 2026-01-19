package com.indian.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarSelectionAdapter(
    private val items: List<CalendarItem>,
    private val onClick: (CalendarItem) -> Unit
) : RecyclerView.Adapter<CalendarSelectionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // જો નામ ન હોય તો "Unknown" બતાવશે, જેથી આપણને ખબર પડે કે આઇટમ લોડ થઈ છે
        holder.txtName.text = item.name ?: "Unnamed Calendar"
        holder.txtName.setTextColor(Color.parseColor("#000000")) // ઘાટો કાળો કલર
        holder.txtName.setPadding(40, 50, 40, 50)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount() = items.size
}
