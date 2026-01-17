
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

        // અહીં આપણે સેફ કોલ વાપરીશું જેથી જો ID અલગ હોય તો બિલ્ડ ન અટકે
        val txtDate = view.findViewById<TextView>(R.id.txtDate) ?: view.findViewById(R.id.txtEnglishDate)
        val txtDetail = view.findViewById<TextView>(R.id.txtTithi) ?: view.findViewById(R.id.txtGujarati)

        dayData?.let {
            txtDate?.text = it.Date
            txtDetail?.text = it.Gujarati
        }
        return view
    }
}
