package com.indian.calendar

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
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

    // ભાષાઓની યાદી
    private val languages = arrayOf("Gujarati", "Hindi", "Marathi", "English")
    private var currentTranslator: Translator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        languageSpinner = findViewById(R.id.languageSpinner)

        setupLanguageMenu()
    }

    private fun setupLanguageMenu() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedLang = languages[position]
                prepareTranslatorAndFetchData(selectedLang)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun prepareTranslatorAndFetchData(lang: String) {
        val targetLang = when (lang) {
            "Gujarati" -> TranslateLanguage.GUJARATI
            "Hindi" -> TranslateLanguage.HINDI
            "Marathi" -> TranslateLanguage.MARATHI
            else -> TranslateLanguage.ENGLISH
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLang)
            .build()
        
        currentTranslator = Translation.getClient(options)
        
        txtPanchang.text = "ભાષા મોડેલ ડાઉનલોડ થઈ રહ્યું છે..."
        
        currentTranslator?.downloadModelIfNeeded()
            ?.addOnSuccessListener {
                fetchSheetData()
            }
            ?.addOnFailureListener {
                txtPanchang.text = "Error: ભાષા મોડેલ ડાઉનલોડ ન થઈ શક્યું."
            }
    }

    private fun fetchSheetData() {
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val todayDate = sdf.format(Date())
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread { txtPanchang.text = "ઇન્ટરનેટ કનેક્શન નથી." }
            }

            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                val lines = csvContent.split("\n")

                for (line in lines) {
                    val row = line.split(",")
                    if (row.isNotEmpty() && row[0].contains(todayDate)) {
                        // માની લો કે અંગ્રેજી પંચાંગ ડેટા કોલમ ૨ માં છે
                        val englishText = "Gujarati: ${row[2]}\nIslamic: ${row[4]}"
                        
                        currentTranslator?.translate(englishText)
                            ?.addOnSuccessListener { translatedText ->
                                runOnUiThread {
                                    txtDate.text = "આજની તારીખ: ${row[0]}/2026"
                                    txtPanchang.text = translatedText
                                    if (row.size > 30) txtFestival.text = row[30]
                                    if (row.size > 31) txtEmoji.text = row[31]
                                }
                            }
                        break
                    }
                }
            }
        })
    }
}

