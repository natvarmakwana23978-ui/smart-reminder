package com.indian.calendar

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.gson.Gson
import com.google.gson.JsonObject

class CalendarPagerAdapter(
    fa: FragmentActivity, 
    private val fullJsonString: String, // આખી JSON String લો
    private val lang: String, 
    private val calIndex: Int
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = 12

    override fun createFragment(position: Int): Fragment {
        // આખું JSON સ્ટ્રિંગ તરીકે જ Fragment માં મોકલી દો
        // ત્યાં Fragment ની અંદર આપણે મહિનો અને કેલેન્ડર ઇન્ડેક્સ મુજબ ડેટા ફિલ્ટર કરીશું
        return MonthFragment.newInstance(fullJsonString, lang, calIndex, position + 1)
    }
}
