package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarListAdapter(
    private var daysList: List<Pair<String, String?>>, // તારીખ અને નોંધ
    private val onEditClick: (String) -> Unit // એડિટ બટન માટેની એક્શન
) : RecyclerView.Adapter<CalendarListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtDate: TextView = view.findViewById(R.id.txtRowDate)
        val txtNote: TextView = view.findViewById(R.id.txtRowNote)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEditNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (date, note) = daysList[position]
        holder.txtDate.text = date
        holder.txtNote.text = note ?: "કોઈ નોંધ નથી"

        holder.btnEdit.setOnClickListener {
            onEditClick(date)
        }
    }

    override fun getItemCount() = daysList.size

    // સર્ચ કરવા માટે ડેટા અપડેટ કરવાનું ફંક્શન
    fun updateList(newList: List<Pair<String, String?>>) {
        daysList = newList
        notifyDataSetChanged()
    }
}
