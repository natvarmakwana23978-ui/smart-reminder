package com.indian.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat  // આ જરૂરી છે
import java.util.Locale          // આ પણ જરૂરી છે

class ManageCalendarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_calendar)
        
        // બાકીનો તમારો કોડ અહીં હશે...
        // SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    }
}

