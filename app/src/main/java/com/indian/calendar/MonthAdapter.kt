package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.indian.calendar.R // આ લાઈન હોવી જ જોઈએ

class MonthAdapter(
    private val monthData: List<JsonObject>,
    private val selectedHeader: String
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    // અહીં ID મેચ કરો (rvDays)
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val rvDays: RecyclerView? = v.findViewById(R.id.rvDays) 
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // ખાતરી કરો કે તમારી layout ફાઈલનું નામ item_month જ છે
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val daysList = monthData.map { 
            CalendarDayData(it.get("ENGLISH")?.asString ?: "", it) 
        }
        
        holder.rvDays?.apply {
            layoutManager = GridLayoutManager(context, 7)
            adapter = CalendarAdapter(daysList, selectedHeader)
        }
    }

    override fun getItemCount() = 1
}
