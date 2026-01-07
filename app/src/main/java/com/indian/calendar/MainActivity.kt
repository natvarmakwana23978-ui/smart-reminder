// Spinner (Dropdown) માટેનું સાચું લોજિક
spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val selectedItem = parent.getItemAtPosition(position).toString()
        
        if (selectedItem == "પર્સનલ નોંધ ઉમેરો") {
            // ખાતરી કરો કે ManageCalendarActivity મેનિફેસ્ટમાં એડ કરેલી છે
            val intent = Intent(this@MainActivity, ManageCalendarActivity::class.java)
            startActivity(intent)
        } else if (selectedItem == "ગુજરાતી") {
            // ગુજરાતી કેલેન્ડર અપડેટ કરો
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}

