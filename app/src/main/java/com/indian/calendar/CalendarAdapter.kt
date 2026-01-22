override fun onBindViewHolder(h: ViewHolder, pos: Int) {
    val d = days[pos]
    
    // ૧. ઓફસેટ (ખાલી) દિવસોને છુપાવો [cite: 2026-01-21]
    if (d.englishDate.isNullOrEmpty()) {
        h.itemView.visibility = View.INVISIBLE
        return
    }
    h.itemView.visibility = View.VISIBLE
    
    // ૨. માત્ર અંગ્રેજી તારીખનો આંકડો સેટ કરો [cite: 2026-01-21]
    val parts = d.englishDate.split(" ")
    h.tvEng.text = if (parts.size >= 3) parts[2] else ""
    
    // ૩. ગુજરાતી તિથિ સેટ કરો [cite: 2026-01-21]
    h.tvLoc.text = d.localDate ?: ""

    // ૪. લાલ પટ્ટી ફિલ્ટર (માત્ર ભારતીય તહેવારો બતાવવા માટે) [cite: 2026-01-21]
    val alertText = d.alert ?: ""
    
    // આ લિસ્ટમાં રહેલા શબ્દો લાલ પટ્ટીમાં દેખાશે નહીં [cite: 2026-01-21]
    val filterWords = listOf(
        "Tevet", "Shevat", "Adar", "Nisan", "Iyar", "Sivan", 
        "Tamuz", "Av", "Elul", "Tishrei", "Chesh", "Kislev",
        "Rajab", "Shaban", "Ramadan", "Shawwal"
    )
    
    val isInvalid = filterWords.any { alertText.contains(it, ignoreCase = true) }

    if (alertText.isNotEmpty() && !isInvalid) {
        h.tvAlert.visibility = View.VISIBLE
        h.tvAlert.text = alertText
    } else {
        // જો લખાણ ખોટું હોય અથવા ખાલી હોય તો પટ્ટી છુપાવી દો [cite: 2026-01-21]
        h.tvAlert.visibility = View.GONE
    }
}
