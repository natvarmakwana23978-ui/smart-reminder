package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val days: List<CalendarDayData>) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
        val tvAlert: TextView = v.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int) = 
        ViewHolder(LayoutInflater.from(p.context).inflate(R.layout.item_calendar_day, p, false))

    override fun onBindViewHolder(h: ViewHolder, pos: Int) {
        val d = days[pos]
        
        // જો ઓફસેટ (ખાલી) દિવસ હોય તો આખું કાર્ડ છુપાવો
        if (d.englishDate.isNullOrEmpty()) {
            h.itemView.visibility = View.INVISIBLE
            return
        }
        
        h.itemView.visibility = View.VISIBLE
        
        // ૧. અંગ્રેજી તારીખનો આંકડો (દા.ત. 01, 02)
        val parts = d.englishDate.split(" ")
        h.tvEng.text = if (parts.size >= 3) parts[2] else ""
        
        // ૨. ગુજરાતી તિથિ (દા.ત. સુદ-૧૩)
        h.tvLoc.text = d.localDate ?: ""

        // ૩. લાલ પટ્ટી ફિલ્ટર (માત્ર ભારતીય તહેવારો રાખવા માટે)
        val alertText = d.alert ?: ""
        // જે શબ્દો આપણે નથી બતાવવા તેનું લિસ્ટ
        val filterWords = listOf("Tevet", "Shevat", "Adar", "Nisan", "Iyar", "Sivan", "Rajab", "Shaban")
        
        val isInvalid = filterWords.any { alertText.contains(it, ignoreCase = true) }

        if (alertText.isNotEmpty() && !isInvalid) {
            h.tvAlert.visibility = View.VISIBLE
            h.tvAlert.text = alertText
        } else {
            // જો ફિલ્ટર લિસ્ટમાં હોય અથવા ખાલી હોય તો પટ્ટી છુપાવો
            h.tvAlert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
