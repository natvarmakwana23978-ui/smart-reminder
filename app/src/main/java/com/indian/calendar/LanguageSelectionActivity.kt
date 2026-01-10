package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class LanguageSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listView = ListView(this)
        val languages = arrayOf("ગુજરાતી", "Hindi", "English", "Marathi", "Tamil")
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, languages)
        setContentView(listView)

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("lang", languages[position])
            startActivity(intent)
        }
    }
}
