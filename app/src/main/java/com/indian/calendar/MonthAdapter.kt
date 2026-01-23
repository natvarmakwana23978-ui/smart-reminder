package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject

class MonthAdapter(
    private val monthData: List<JsonObject>,
    private val selectedHeader: String
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val rvDays: RecyclerView = v.findViewById(R.id.rvDays)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_month, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // આખા મહિનાનો ડેટા મેપ કરો
        val daysList = monthData.map { 
            // અહીં માત્ર ૨ જ આર્ગ્યુમેન્ટ આપો: ENGLISH તારીખ અને આખો JsonObject
            CalendarDayData(it.get("ENGLISH")?.asString ?: "", it) 
        }
        
        holder.rvDays.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        // CalendarAdapter ને સિલેક્ટેડ હેડર પણ પાસ કરો
        holder.rvDays.adapter = CalendarAdapter(daysList, selectedHeader)
    }

    override fun getItemCount() = 1 // જરૂર મુજબ બદલી શકાય
}
