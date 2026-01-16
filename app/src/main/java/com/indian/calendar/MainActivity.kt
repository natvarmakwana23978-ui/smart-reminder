package com.indian.calendar

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // "કેલેન્ડર જૂઓ" બટન
        val btnViewCalendar = findViewById<Button>(R.id.btnViewCalendar)
        btnViewCalendar.setOnClickListener {
            try {
                val intent = Intent(this, CalendarSelectionActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "ભૂલ આવી: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        // બાકીના બે બટન અત્યારે ખાલી રાખીએ
        findViewById<Button>(R.id.btnCreateCalendar).setOnClickListener {
            Toast.makeText(this, "આ ફીચર ટૂંક સમયમાં આવશે", Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.btnReminders).setOnClickListener {
            Toast.makeText(this, "રીમાઇન્ડર ફીચર પર કામ ચાલુ છે", Toast.LENGTH_SHORT).show()
        }
    }
}
