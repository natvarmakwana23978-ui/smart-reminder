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
    private lateinit var calendarSpinner: Spinner
    private lateinit var languageSpinner: Spinner
    private var currentTranslator: Translator? = null

    // તમારી શીટની તમામ ૨૬ કેલેન્ડર કોલમ
    private val calendarOptions = listOf(
        "ગુજરાતી (Gujarati)" to 2, "હિન્દી (Hindi)" to 3, "ઇસ્લામિક (Islamic)" to 4,
        "તેલુગુ/કન્નડ" to 5, "તમિલ" to 6, "મલયાલમ" to 7, "પંજાબી" to 8, "ઓડિયા" to 9,
        "બંગાળી" to 10, "नेपाली (Nepali)" to 11, "Chinese" to 12, "Hebrew" to 13,
        "Persian" to 14, "Ethiopian" to 15, "Balinese" to 16, "Korean" to 17,
        "Vietnamese" to 18, "Thai" to 19, "French" to 20, "Burmese" to 21,
        "Kashmiri" to 22, "Marwari" to 23, "Japanese" to 24, "Assamese" to 25,
        "Sindhi" to 26, "Tibetan" to 27
    )

    // ML Kit સપોર્ટ કરતી તમામ મહત્વની ભાષાઓ
    private val languageOptions = listOf(
        "Gujarati" to TranslateLanguage.GUJARATI, "Hindi" to TranslateLanguage.HINDI,
        "Marathi" to TranslateLanguage.MARATHI, "English" to TranslateLanguage.ENGLISH,
        "Tamil" to TranslateLanguage.TAMIL, "Telugu" to TranslateLanguage.TELUGU,
        "Bengali" to TranslateLanguage.BENGALI, "Kannada" to TranslateLanguage.KANNADA,
        "Spanish" to TranslateLanguage.SPANISH, "French" to TranslateLanguage.FRENCH,
        "German" to TranslateLanguage.GERMAN, "Chinese" to TranslateLanguage.CHINESE,
        "Japanese" to TranslateLanguage.JAPANESE, "Korean" to TranslateLanguage.KOREAN,
        "Arabic" to TranslateLanguage.ARABIC, "Russian" to TranslateLanguage.RUSSIAN
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        calendarSpinner = findViewById(R.id.calendarSpinner)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupSpinners()
        
        // શરૂઆતમાં ફોનની ભાષા મુજબ ઓટો-ડિટેક્ટ
        autoDetectSettings()
    }

    private fun setupSpinners() {
        calendarSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, calendarOptions.map { it.first })
        languageSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageOptions.map { it.first })

        val listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                updateUI()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        calendarSpinner.onItemSelectedListener = listener
        languageSpinner.onItemSelectedListener = listener
    }

    private fun autoDetectSettings() {
        val systemLang = Locale.getDefault().language
        // ફોનની ભાષા જો ગુજરાતી હોય તો ઓટોમેટિક ગુજરાતી કેલેન્ડર અને ગુજરાતી ભાષા સેટ થશે
        val langIndex = languageOptions.indexOfFirst { it.second == systemLang }
        if (langIndex != -1) languageSpinner.setSelection(langIndex)
        
        val calIndex = when(systemLang) {
            "gu" -> 0 // Gujarati
            "hi" -> 1 // Hindi
            "ar" -> 2 // Islamic
            else -> 0
        }
        calendarSpinner.setSelection(calIndex)
    }

    private fun updateUI() {
        val calendarCol = calendarOptions[calendarSpinner.selectedItemPosition].second
        val targetLang = languageOptions[languageSpinner.selectedItemPosition].second
        prepareTranslatorAndFetch(calendarCol, targetLang)
    }

    private fun prepareTranslatorAndFetch(col: Int, lang: String) {
        currentTranslator?.close()
        val options = TranslatorOptions.Builder().setSourceLanguage(TranslateLanguage.ENGLISH).setTargetLanguage(lang).build()
        currentTranslator = Translation.getClient(options)
        
        txtPanchang.text = "લોડ થઈ રહ્યું છે..."
        
        currentTranslator?.downloadModelIfNeeded()
            ?.addOnSuccessListener { fetchSheetData(col) }
            ?.addOnFailureListener { fetchSheetData(col) } // જો ડાઉનલોડ ન થાય તો સીધો ડેટા બતાવો
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
                        
                        // કેલેન્ડર ડેટા + વાર (Column 28) ને જોડીને પૂર્ણ ફોર્મેટ બનાવો
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

