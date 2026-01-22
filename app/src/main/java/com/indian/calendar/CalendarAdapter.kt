package com.indian.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.json.JSONObject

class CalendarAdapter(
    private val days: List<CalendarDayData>,
    private val selectedHeader: String // યુઝરે પસંદ કરેલ કેલેન્ડરનું નામ (દા.ત. "ગુજરાતી (Gujarati)")
) : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvEng: TextView = v.findViewById(R.id.tvEnglishDate)
        val tvLoc: TextView = v.findViewById(R.id.tvLocalDate)
        val tvAlert: TextView = v.findViewById(R.id.tvAlert)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dayData = days[position]
        
        // ૧. ઓફસેટ (ખાલી) દિવસોને છુપાવો
        if (dayData.englishDate.isNullOrEmpty()) {
            holder.itemView.visibility = View.INVISIBLE
            return
        }
        holder.itemView.visibility = View.VISIBLE
        
        // ૨. અંગ્રેજી તારીખ સેટ કરો (દા.ત. "1", "2")
        // જો તમારી શીટમાં "01/01/2026" ફોર્મેટ હોય તો split("/") વાપરો
        val dateParts = dayData.englishDate.split("/")
        holder.tvEng.text = if (dateParts.isNotEmpty()) dateParts[0] else ""
        
        // ૩. ગુજરાતી તિથિ હંમેશા બતાવો (કોલમ B માંથી)
        holder.tvLoc.text = dayData.localDate ?: ""

        // ૪. ડાયનેમિક ડેટા લોડિંગ (૨૭ માંથી માત્ર સિલેક્ટ કરેલી કોલમ)
        // જો યુઝરે "ગુજરાતી" પસંદ કર્યું હશે, તો માત્ર તે કોલમનો જ ડેટા લેશે.
        // સપ્ટેમ્બર કે એપ્રિલમાં દેખાતો હિબ્રુ ડેટા અહીં આપોઆપ ફિલ્ટર થઈ જશે.
        val alertText = dayData.allData.optString(selectedHeader, "")

        if (alertText.isNotEmpty() && alertText != "null") {
            holder.tvAlert.visibility = View.VISIBLE
            holder.tvAlert.text = alertText
        } else {
            // જો તે તારીખે પસંદ કરેલ કેલેન્ડરમાં કોઈ વિગત ન હોય તો પટ્ટી છુપાવો
            holder.tvAlert.visibility = View.GONE
        }
    }

    override fun getItemCount() = days.size
}
