package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI Elements (તમારી activity_main.xml મુજબના ID)
        val tvGregorianDate = findViewById<TextView>(R.id.tv_gregorian_date)
        val tvVikramSamvat = findViewById<TextView>(R.id.tv_vikram_samvat)
        val tvSpecialDay = findViewById<TextView>(R.id.tv_special_day)

        // આજના દિવસની તારીખ મેળવો
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        
        // ગ્રેગોરિયન તારીખ ડિસ્પ્લે કરવા માટે
        val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        tvGregorianDate.text = displayFormat.format(Date())

        try {
            // Assets માંથી JSON વાંચવા માટે
            val inputStream = assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            var found = false
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    tvVikramSamvat.text = obj.getString("Vikram_Samvat")
                    
                    val special = obj.getString("Special_Day")
                    tvSpecialDay.text = if (special == "--") "આજે કોઈ ખાસ વિગત નથી" else special
                    
                    found = true
                    break
                }
            }

            if (!found) {
                tvVikramSamvat.text = "ડેટા ઉપલબ્ધ નથી"
                tvSpecialDay.text = "આ તારીખ માટે માહિતી મળી નથી ($currentDate)"
            }

        } catch (e: Exception) {
            tvVikramSamvat.text = "ભૂલ: ફાઇલ મળી નથી"
            tvSpecialDay.text = "Assets ચેક કરો"
            e.printStackTrace()
        }
    }
}

