package com.indian.calendar

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.indian.calendar.R
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // તમારા XML લેઆઉટ મુજબના IDs
        val txtGregorianDate = findViewById<TextView>(R.id.txtGregorianDate)
        val txtGregorianDay = findViewById<TextView>(R.id.txtGregorianDay)
        val txtVikramDate = findViewById<TextView>(R.id.txtVikramDate)
        val txtSpecialDay = findViewById<TextView>(R.id.txtSpecialDay)
        val txtTodayEvents = findViewById<TextView>(R.id.txtTodayEvents)

        // આજના દિવસની તારીખ મેળવો
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())
        
        // ગ્રેગોરિયન તારીખ અને વાર ડિસ્પ્લે કરવા માટે
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        
        txtGregorianDate.text = dateFormat.format(Date())
        txtGregorianDay.text = dayFormat.format(Date())

        try {
            // Assets માંથી JSON ફાઈલ વાંચવા માટે
            val inputStream = assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            var found = false
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    // વિક્રમ સંવત સેટ કરો
                    txtVikramDate.text = obj.getString("Vikram_Samvat")
                    
                    // ખાસ દિવસની વિગત
                    val special = obj.getString("Special_Day")
                    if (special == "--") {
                        txtSpecialDay.text = ""
                        txtTodayEvents.text = "આજની તારીખ માટે કોઈ ખાસ વિગત ઉપલબ્ધ નથી."
                    } else {
                        txtSpecialDay.text = special
                        txtTodayEvents.text = "આજે $special છે."
                    }
                    
                    found = true
                    break
                }
            }

            if (!found) {
                txtVikramDate.text = "ડેટા મળ્યો નથી"
                txtTodayEvents.text = "આ તારીખ માટે ડેટાબેઝમાં માહિતી નથી ($currentDate)"
            }

        } catch (e: Exception) {
            txtVikramDate.text = "ફાઈલ એરર!"
            txtTodayEvents.text = "Assets ફોલ્ડરમાં JSON ફાઈલ તપાસો."
            e.printStackTrace()
        }
    }
}
