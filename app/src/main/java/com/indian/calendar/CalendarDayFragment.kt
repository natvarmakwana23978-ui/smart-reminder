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
        // fragment_calendar_day લેઆઉટ ઇન્ફ્લેટ કરો
        val view = inflater.inflate(R.layout.fragment_calendar_day, container, false)
        val dayData = arguments?.getParcelable<CalendarDayData>("day_data")

        // જો સાચા ID ન મળતા હોય, તો આખા વ્યુમાં રહેલા TextView શોધો
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is TextView) {
                    // પ્રથમ TextView માં તારીખ અને બીજામાં વિગત મૂકો (જો ID ખબર ન હોય તો આ દેશી જુગાડ છે)
                    if (i == 0) child.text = dayData?.Date
                    if (i == 1) child.text = dayData?.Gujarati
                }
            }
        }

        return view
    }
}
