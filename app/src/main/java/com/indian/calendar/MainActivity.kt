package com.indian.calendar

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    // UI ના ઘટકો
    private lateinit var txtDate: TextView
    private lateinit var txtPanchang: TextView
    private lateinit var txtFestival: TextView
    private lateinit var txtEmoji: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI ઘટકોને ID સાથે જોડવા
        txtDate = findViewById(R.id.txtDate)
        txtPanchang = findViewById(R.id.txtPanchang)
        txtFestival = findViewById(R.id.txtFestival)
        txtEmoji = findViewById(R.id.txtEmoji)
        
        // જો તમે layout માં ProgressBar મૂક્યો હોય તો
        // progressBar = findViewById(R.id.progressBar)

        fetchTodayPanchang()
    }

    private fun fetchTodayPanchang() {
        // ૧. આજની તારીખ મેળવો (ફોર્મેટ: dd/MM)
        val sdf = SimpleDateFormat("dd/MM", Locale.getDefault())
        val todayDate = sdf.format(Date())

        // ૨. ગૂગલ શીટની લિંક (CSV એક્સપોર્ટ મોડમાં)
        val url = "https://docs.google.com/spreadsheets/d/1CuG14L_0yLveVDpXzKD80dy57yMu7TDWVdzEgxcOHdU/export?format=csv"

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        // ૩. ઇન્ટરનેટ દ્વારા ડેટા ખેંચવો
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    txtPanchang.text = "નેટવર્ક એરર! ઇન્ટરનેટ ચાલુ કરો."
                    txtPanchang.setTextColor(Color.RED)
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val csvContent = response.body?.string() ?: ""
                
                // CSV ની લાઈનો અલગ કરવી
                val lines = csvContent.split("\n")

                var found = false
                for (line in lines) {
                    // કોલમ અલગ કરવી (સ્પ્લિટ બાય કોમા)
                    val row = line.split(",")
                    
                    // જો પહેલી કોલમ આજની તારીખ (dd/MM) ધરાવતી હોય
                    if (row.isNotEmpty() && row[0].contains(todayDate)) {
                        found = true
                        runOnUiThread {
                            // ડેટા સ્ક્રીન પર બતાવવો
                            txtDate.text = "આજની તારીખ: ${row[0]}/2026"
                            
                            val panchangDetail = """
                                🔸 ગુજરાતી: ${row[2]}
                                🔹 હિન્દી: ${row[3]}
                                ☪️ ઇસ્લામિક: ${row[4]}
                                🗓️ વાર: ${if(row.size > 29) row[29] else ""}
                            """.trimIndent()
                            
                            txtPanchang.text = panchangDetail
                            
                            // તહેવાર અને ઇમોજી (જો હોય તો)
                            if (row.size > 30 && row[30].trim().isNotEmpty()) {
                                txtFestival.text = row[30]
                            } else {
                                txtFestival.text = "આજે કોઈ ખાસ તહેવાર નથી"
                            }
                            
                            if (row.size > 31) {
                                txtEmoji.text = row[31]
                            }
                        }
                        break
                    }
                }

                if (!found) {
                    runOnUiThread {
                        txtPanchang.text = "આજની તારીખનો ડેટા મળ્યો નથી."
                    }
                }
            }
        })
    }
}

