package com.smart.reminder
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smart.reminder.databinding.ItemCalendarDayBinding

class CalendarAdapter(private val list: List<CalendarDayData>, private val langIndex: Int) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemCalendarDayBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCalendarDayBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.binding.txtDate.text = item.date
        holder.binding.txtDetails.text = item.dataList.getOrNull(langIndex) ?: ""
        holder.binding.txtFestival.text = "${item.emoji} ${item.festival}"
    }
    override fun getItemCount(): Int = list.size
}
