package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.indian.calendar.model.CalendarDayData

class CalendarDayFragment : Fragment() {

    companion object {
        fun newInstance(dayData: CalendarDayData): CalendarDayFragment {
            val fragment = CalendarDayFragment()
            val args = Bundle()
            args.putParcelable("day_data", dayData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_day, container, false)
        val dayData = arguments?.getParcelable<CalendarDayData>("day_data")

        // નોંધ: ખાતરી કરજો કે તમારા fragment_calendar_day.xml માં આ ID હોય, 
        // જો ન હોય તો જે ID હોય તે અહીં લખવા (દા.ત. R.id.txtEnglishDate)
        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtDetail = view.findViewById<TextView>(R.id.txtTithi) // અહીં ID ચેક કરી લેજો

        dayData?.let {
            txtDate?.text = it.Date
            txtDetail?.text = it.Gujarati
        }
        return view
    }
}
