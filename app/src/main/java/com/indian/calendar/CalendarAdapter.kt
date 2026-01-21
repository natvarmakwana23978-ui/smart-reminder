override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val day = days[position]
    
    // "Thu Jan 01 2026 00:0" માંથી માત્ર "01" અલગ પાડવા માટે [cite: 2026-01-21]
    val parts = day.englishDate?.split(" ")
    val dateNumber = if (parts != null && parts.size >= 3) parts[2] else ""
    
    holder.tvEnglishDate.text = dateNumber
    holder.tvLocalDate.text = day.localDate ?: ""
    
    // સમય (00:0) હવે આપોઆપ દૂર થઈ જશે કારણ કે આપણે માત્ર તારીખનો આંકડો જ લઈએ છીએ [cite: 2026-01-21]
}
