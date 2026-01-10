package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // સિમ્પલ લેઆઉટ પ્રોગ્રામેટિકલી બનાવીએ છીએ જેથી નવી XML ની ઝંઝટ ના રહે
        val listView = ListView(this)
        val languages = arrayOf("ગુજરાતી", "Hindi", "English", "Marathi", "Tamil", "Telugu", "Kannada")
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)
        listView.adapter = adapter

        setContentView(listView)

        listView.setOnItemClickListener { _, _, position, _ ->
            val selectedLang = languages[position]
            // ભાષા પસંદ કર્યા પછી મેઈન કેલેન્ડર (પગલું ૬) પર જશે
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("selected_language", selectedLang)
            startActivity(intent)
        }
    }
}

