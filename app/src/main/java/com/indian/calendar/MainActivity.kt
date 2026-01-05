package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
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
    
    private var currentTranslator: Translator? = null
    private var selectedTargetLang = TranslateLanguage.GUJARATI
    private var currentCalendarCol = 2 // Default Gujarati Calendar

    private val calendarOptions = listOf(
        "ગુજરાતી કેલેન્ડર" to 2,
        "હિન્દી કેલેન્ડર" to 6,
        "મરાઠી કેલેન્ડર" to 10,
        "તારીખિયું (English)" to 1,
        "➕ પર્સનલ નોંધ ઉમેરો" to 99
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        calendarSpinner = findViewById(R.id.calendarSpinner)

        setupTranslator()
        setupCalendarSpinner()
        fetchSheetData()
    }

    private fun setupTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(selectedTargetLang)
            .build()
        currentTranslator = Translation.getClient(options)
        
        currentTranslator?.downloadModelIfNeeded()?.addOnSuccessListener {
            fetchSheetData()
        }
    }

    private fun setupCalendarSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarOptions.map { it.first })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calendarSpinner.adapter = adapter

        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selection = calendarOptions[position]
                if (selection.second == 99) {
                    val intent = Intent(this@MainActivity, ManageCalendarActivity::class.java)
                    startActivity(intent)
                    calendarSpinner.setSelection(0)
                } else {
                    currentCalendarCol = selection.second
                    fetchSheetData()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchSheetData() {
        val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val csvData = response.body?.string() ?: return
                val rows = csvData.split("\n")
                
                for (line in rows) {
                    val columns = line.split(",")
                    if (columns.isNotEmpty() && columns[0].trim() == today) {
                        val rawPanchang = columns.getOrNull(currentCalendarCol) ?: ""
                        val dayName = columns.getOrNull(28) ?: ""
                        val rawFestival = columns.getOrNull(30) ?: ""
                        val emoji = columns.getOrNull(31) ?: ""

                        // તહેવાર અને પંચાંગનું ભાષાંતર
                        val textToTranslate = "$rawPanchang | $rawFestival | $dayName"
                        
                        currentTranslator?.translate(textToTranslate)?.addOnSuccessListener { translatedText ->
                            val parts = translatedText.split("|")
                            val cleanPanchang = applyCorrections(parts.getOrNull(0) ?: "")
                            val cleanFestival = parts.getOrNull(1) ?: ""
                            val cleanDay = parts.getOrNull(2) ?: ""

                            runOnUiThread {
                                updateUI(columns[0], cleanPanchang, cleanFestival, emoji, cleanDay)
                            }
                        }
                        break
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }

    private fun applyCorrections(input: String): String {
        return input.replace("વાડ", "વદ")
            .replace("સુદિ", "સુદ")
            .replace("બીજ", "દુિતયા")
            .trim()
    }

    private fun updateUI(date: String, panchang: String, festival: String, emoji: String, day: String) {
        txtDate.text = "$date/2026"
        txtPanchang.text = "$panchang, $day"
        txtEmoji.text = emoji
        
        // પર્સનલ નોંધ ડેટાબેઝમાંથી લાવો
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@MainActivity)
            val note = db.userNoteDao().getNoteByDate(date)
            
            if (note != null && note.personalNote.isNotEmpty()) {
                txtFestival.text = "$festival\n\n📌 નોંધ: ${note.personalNote}"
            } else {
                txtFestival.text = festival
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        currentTranslator?.close()
    }
}

