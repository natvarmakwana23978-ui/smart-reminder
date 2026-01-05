package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.*
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var txtEmoji: TextView
    private lateinit var btnSelectLanguage: Button // સ્પિનરની જગ્યાએ બટન (સર્ચ માટે)
    private lateinit var calendarSpinner: Spinner
    
    private var currentTranslator: Translator? = null
    private var selectedTargetLang = TranslateLanguage.GUJARATI // ડિફોલ્ટ
    private var currentCalendarCol = 2 // ડિફોલ્ટ ગુજરાતી

    // ૧. ૨૬ સ્ટાન્ડર્ડ + ૧ કસ્ટમ કેલેન્ડર
    private val calendarOptions = mutableListOf(
        "ગુજરાતી" to 2, "હિન્દી" to 3, "ઇસ્લામિક" to 4, "તેલુગુ/કન્નડ" to 5,
        "તમિલ" to 6, "મલયાલમ" to 7, "પંજાબી" to 8, "ઓડિયા" to 9, "બંગાળી" to 10,
        "નેપાલી" to 11, "Chinese" to 12, "Hebrew" to 13, "Persian" to 14,
        "Ethiopian" to 15, "Balinese" to 16, "Korean" to 17, "Vietnamese" to 18,
        "Thai" to 19, "French" to 20, "Burmese" to 21, "Kashmiri" to 22,
        "Marwari" to 23, "Japanese" to 24, "Assamese" to 25, "Sindhi" to 26,
        "Tibetan" to 27, "Personal (કસ્ટમ કેલેન્ડર)" to 99
    )

    // ૨. ML Kit ની તમામ ૫૦+ ભાષાઓનું લિસ્ટ
    private val allLanguages = TranslateLanguage.getAllLanguages().map { 
        Locale(it).displayLanguage to it 
    }.sortedBy { it.first }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        calendarSpinner = findViewById(R.id.calendarSpinner)
        btnSelectLanguage = findViewById(R.id.btnSelectLanguage)

        setupCalendarSpinner()
        
        // ભાષા પસંદ કરવા માટે સર્ચ ડાયલોગ ખોલો
        btnSelectLanguage.setOnClickListener {
            showLanguageSearchDialog()
        }
    }

    private fun setupCalendarSpinner() {
        calendarSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarOptions.map { it.first })
        calendarSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val selection = calendarOptions[pos]
                if (selection.second == 99) {
                    // કસ્ટમ કેલેન્ડર પસંદ કર્યું - ૩૬૫ દિવસના મેનેજમેન્ટ પેજ પર જાવ
                    Toast.makeText(this@MainActivity, "કસ્ટમ કેલેન્ડર મોડ", Toast.LENGTH_SHORT).show()
                    // અહીં આપણે ભવિષ્યમાં Intent ઉમેરીશું
                } else {
                    currentCalendarCol = selection.second
                    updateUI()
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun showLanguageSearchDialog() {
        val langNames = allLanguages.map { it.first }.toTypedArray()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ભાષા પસંદ કરો (Search Language)")
        
        builder.setItems(langNames) { _, which ->
            val selected = allLanguages[which]
            selectedTargetLang = selected.second
            btnSelectLanguage.text = "ભાષા: ${selected.first}"
            updateUI()
        }
        builder.show()
    }

    private fun updateUI() {
        prepareTranslatorAndFetch(currentCalendarCol, selectedTargetLang)
    }

    private fun prepareTranslatorAndFetch(col: Int, lang: String) {
        currentTranslator?.close()
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(lang)
            .build()
        currentTranslator = Translation.getClient(options)
        
        txtPanchang.text = "પ્રોસેસિંગ..."
        currentTranslator?.downloadModelIfNeeded()?.addOnSuccessListener { fetchSheetData(col) }
    }

    private fun fetchSheetData(colIndex: Int) {
        val today = SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val csv = response.body?.string() ?: ""
                val lines = csv.split("\n")
                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].trim() == today) {
                        val rawData = row.getOrNull(colIndex) ?: ""
                        val dayName = row.getOrNull(28) ?: ""
                        val fullFormat = "$rawData, $dayName"

                        currentTranslator?.translate(fullFormat)?.addOnSuccessListener { translated ->
                            runOnUiThread {
                                txtDate.text = "${row[0]}/2026"
                                txtPanchang.text = translated
                                txtFestival.text = row.getOrNull(30) ?: ""
                                txtEmoji.text = row.getOrNull(31) ?: ""
                            }
                        }
                        break
                    }
                }
            }
            override fun onFailure(call: Call, e: IOException) {}
        })
    }
}

