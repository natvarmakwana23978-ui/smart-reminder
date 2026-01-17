package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.indian.calendar.model.CalendarDayData

class CalendarDayFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_calendar_day, container, false)
        val dayData = arguments?.getParcelable<CalendarDayData>("day_data")

        val txtDate = view.findViewById<TextView>(R.id.txtDate)
        val txtDetail = view.findViewById<TextView>(R.id.txtDetail)

        dayData?.let {
            txtDate.text = it.Date // ENGLISH તારીખ
            txtDetail.text = it.Gujarati // ગુજરાતી વિગત (તિથિ/તહેવાર)
        }
        return view
    }
}
