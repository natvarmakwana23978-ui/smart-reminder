package com.indian.calendar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerLang = findViewById<Spinner>(R.id.spinnerLanguage)
        val spinnerCal = findViewById<Spinner>(R.id.spinnerCalendarType)
        val btnSearch = findViewById<Button>(R.id.btnActionSearch)
        val txtTithi = findViewById<TextView>(R.id.txtTithiDisplay)

        // ૧. ભાષાના ઓપ્શન્સ
        val languages = arrayOf("ગુજરાતી", "Hindi", "English", "Sinhala", "Custom (તમારી ભાષા)")
        val langAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, languages)
        spinnerLang.adapter = langAdapter

        // ૨. કેલેન્ડરના ઓપ્શન્સ (Google Sheets + નવું બનાવવું)
        val calendars = arrayOf(
            "મુખ્ય સરકારી કેલેન્ડર (Google Sheet)",
            "બેંક રજાઓનું કેલેન્ડર",
            "સ્થાનિક સમુદાય કેલેન્ડર",
            "+ નવું કેલેન્ડર બનાવો / સુધારો"
        )
        val calAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, calendars)
        spinnerCal.adapter = calAdapter

        // પસંદગી મુજબ કામ કરવું
        spinnerCal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selected = calendars[position]
                if (selected == "+ નવું કેલેન્ડર બનાવો / સુધારો") {
                    // યુઝરને એડિટ/ક્રિએટ સ્ક્રીન પર લઈ જવો
                    val intent = Intent(this@MainActivity, ManageCalendarActivity::class.java)
                    startActivity(intent)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSearch.setOnClickListener {
            val lang = spinnerLang.selectedItem.toString()
            Toast.makeText(this, "$lang માં ડેટા લોડ થઈ રહ્યો છે...", Toast.LENGTH_SHORT).show()
        }
    }
}

