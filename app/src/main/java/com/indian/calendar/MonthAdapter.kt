package com.smart.reminder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.reminder.databinding.ItemCalendarDayBinding

class MonthAdapter(
    private val dayList: List<CalendarDayData>,
    private val langIndex: Int
) : RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayData = dayList[position]
        
        holder.binding.txtDate.text = dayData.date
        // 'details' ની ભૂલ અહીં સુધારી - dataList[langIndex] વાપરો
        holder.binding.txtDetails.text = dayData.dataList.getOrNull(langIndex) ?: ""
        holder.binding.txtFestival.text = "${dayData.emoji} ${dayData.festival}"

        // રવિવાર અને કલર કોડ માટેનું લોજિક
        if (dayData.religious.contains("Sunday", ignoreCase = true)) {
            holder.binding.root.setBackgroundColor(Color.parseColor("#FFEBEE")) // આછો લાલ
        } else {
            holder.binding.root.setBackgroundColor(Color.WHITE)
        }
    }

    override fun getItemCount(): Int = dayList.size
}
