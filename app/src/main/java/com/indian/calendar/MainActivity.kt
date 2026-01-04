package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
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
    private lateinit var languageSpinner: Spinner
    private var currentTranslator: Translator? = null

    private val languageMap = linkedMapOf(
        "Gujarati" to TranslateLanguage.GUJARATI,
        "Hindi" to TranslateLanguage.HINDI,
        "Marathi" to TranslateLanguage.MARATHI,
        "Tamil" to TranslateLanguage.TAMIL,
        "Telugu" to TranslateLanguage.TELUGU,
        "Bengali" to TranslateLanguage.BENGALI,
        "Kannada" to TranslateLanguage.KANNADA,
        "Spanish" to TranslateLanguage.SPANISH,
        "French" to TranslateLanguage.FRENCH,
        "German" to TranslateLanguage.GERMAN,
        "Arabic" to TranslateLanguage.ARABIC,
        "Chinese" to TranslateLanguage.CHINESE,
        "Japanese" to TranslateLanguage.JAPANESE,
        "Korean" to TranslateLanguage.KOREAN,
        "Russian" to TranslateLanguage.RUSSIAN,
        "Thai" to TranslateLanguage.THAI
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupLanguageMenu()
        
        // --- Auto Detect Logic Start ---
        autoDetectAndSetLanguage()
        // --- Auto Detect Logic End ---
    }

    private fun autoDetectAndSetLanguage() {
        val systemLangCode = Locale.getDefault().language // ફોનની ભાષાનો કોડ (hi, gu, en વગેરે)
        
        // ML Kit કોડ સાથે મેચ કરો
        val targetLang = when (systemLangCode) {
            "gu" -> TranslateLanguage.GUJARATI
            "hi" -> TranslateLanguage.HINDI
            "mr" -> TranslateLanguage.MARATHI
            "ta" -> TranslateLanguage.TAMIL
            "te" -> TranslateLanguage.TELUGU
            "bn" -> TranslateLanguage.BENGALI
            "kn" -> TranslateLanguage.KANNADA
            "es" -> TranslateLanguage.SPANISH
            "fr" -> TranslateLanguage.FRENCH
            "de" -> TranslateLanguage.GERMAN
            else -> TranslateLanguage.ENGLISH // જો મેચ ન થાય તો ઇંગ્લિશ
        }

        // સ્પિનરમાં તે ભાષા સિલેક્ટ કરો (યુઝરને ખબર પડે કઈ ભાષા સેટ થઈ છે)
        val langNameList = languageMap.values.toList()
        val index = langNameList.indexOf(targetLang)
        if (index != -1) {
            languageSpinner.setSelection(index)
        }
        
        prepareTranslator(targetLang)
    }

    private fun setupLanguageMenu() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageMap.keys.toList())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                val selectedLang = languageMap.values.toList()[pos]
                prepareTranslator(selectedLang)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }
    }

    private fun prepareTranslator(targetLang: String) {
        currentTranslator?.close()
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLang)
            .build()
        currentTranslator = Translation.getClient(options)
        
        currentTranslator?.downloadModelIfNeeded()
            ?.addOnSuccessListener { fetchSheetData() }
    }

    private fun fetchSheetData() {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val today = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        OkHttpClient().newCall(Request.Builder().url(url).build()).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.split("\n")
                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].trim() == today) {
                        val engText = "System: ${row[2]}\nIslamic: ${row[4]}"
                        currentTranslator?.translate(engText)?.addOnSuccessListener { translated ->
                            runOnUiThread {
                                txtDate.text = "તારીખ: ${row[0]}/2026"
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

