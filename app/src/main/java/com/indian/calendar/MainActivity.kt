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

        // ટેસ્ટિંગ માટે અત્યારે આપણે "Jewish" સેટ કરીએ છીએ
        val selectedCalendar = "Jewish"

        val txtGregorianDate = findViewById<TextView>(R.id.txtGregorianDate)
        val txtVikramDate = findViewById<TextView>(R.id.txtVikramDate) // કાર્ડ ૨
        val txtSpecialDay = findViewById<TextView>(R.id.txtSpecialDay) // કાર્ડ ૩

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = sdf.format(Date())

        try {
            val inputStream = assets.open("json/calendar_2082.json")
            val jsonText = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonText)

            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                if (obj.getString("Date") == currentDate) {
                    // કાર્ડ ૧: ગ્રેગોરિયન
                    txtGregorianDate.text = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date())

                    // કાર્ડ ૨: હિબ્રુ તારીખ (વાર સિસ્ટમ માંથી આવશે)
                    val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
                    txtVikramDate.text = "${obj.getString(selectedCalendar)} ($dayName)"

                    // કાર્ડ ૩: તહેવાર
                    val special = obj.getString("Special_Day")
                    txtSpecialDay.text = if (special == "--") "કોઈ વિશેષ દિવસ નથી" else special
                    break
                }
            }
        } catch (e: Exception) { e.printStackTrace() }
    }
}
