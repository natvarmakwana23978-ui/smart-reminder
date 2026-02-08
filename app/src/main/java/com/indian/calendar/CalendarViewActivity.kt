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

        // તમારા વ્યુપેજરનું ID ચેક કરી લેવું, અત્યારે મેં સામાન્ય નામ રાખ્યું છે
        val adapter = CalendarPagerAdapter(this)
        // જો XML માં ViewPager2 હોય તો જ આ લાઈન કામ કરશે
        // binding.yourViewPagerId.adapter = adapter
    }
}
