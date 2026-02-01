package com.indian.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class MonthFragment : Fragment() {

    companion object {
        fun newInstance(monthData: List<JsonObject>, lang: String): MonthFragment {
            val fragment = MonthFragment()
            val args = Bundle()
            args.putString("MONTH_DATA", Gson().toJson(monthData))
            args.putString("LANG", lang)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_month, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rvMonthCalendar)
        
        val jsonData = arguments?.getString("MONTH_DATA")
        val lang = arguments?.getString("LANG") ?: "ગુજરાતી (Gujarati)"
        
        val dataList: List<JsonObject> = Gson().fromJson(jsonData, object : TypeToken<List<JsonObject>>() {}.type)
        val finalItems = dataList.map { CalendarDayData(it.get("ENGLISH").asString, it) }

        rv.layoutManager = GridLayoutManager(context, 7)
        rv.adapter = CalendarAdapter(finalItems, lang)
        
        return view
    }
}

