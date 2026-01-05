package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var txtEmoji: TextView
    private lateinit var calendarSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        calendarSpinner = findViewById(R.id.calendarSpinner)

        setupCalendarSpinner()
        fetchData()
    }

    private fun setupCalendarSpinner() {
        val options = listOf("ગુજરાતી", "હિન્દી", "➕ પર્સનલ નોંધ ઉમેરો")
        calendarSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                if (pos == 2) {
                    startActivity(Intent(this@MainActivity, ManageCalendarActivity::class.java))
                } else {
                    fetchData()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun fetchData() {
        val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"
        
        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val rows = response.body?.string()?.split("\n") ?: return
                for (rowStr in rows) {
                    val cols = rowStr.split(",")
                    if (cols.isNotEmpty() && cols[0].trim() == today) {
                        runOnUiThread {
                            updateUI(cols[0], cols[2], cols[30], cols[31])
                        }
                        break
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun updateUI(date: String, panchang: String, fest: String, emoji: String) {
        txtDate.text = "$date/2026"
        txtPanchang.text = panchang.replace("વાડ", "વદ")
        txtEmoji.text = emoji
        
        lifecycleScope.launch {
            val note = AppDatabase.getDatabase(this@MainActivity).userNoteDao().getNoteByDate(date)
            txtFestival.text = if (note != null) "$fest\n\n📌 નોંધ: ${note.personalNote}" else fest
        }
    }
}

