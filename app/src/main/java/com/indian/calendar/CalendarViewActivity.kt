package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.databinding.ActivityCalendarViewBinding

class CalendarViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // અહીં વ્યુપેજરનો ઉપયોગ કરીએ છીએ
        val adapter = CalendarPagerAdapter(this)
        binding.viewPager.adapter = adapter 
        // જો XML માં ID 'viewPager' ન હોય તો તે મુજબ બદલવું પડશે
    }
}
