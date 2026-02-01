override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
    val data = items[position].allData
    val engDate = items[position].englishDate // સાચું નામ 'englishDate' છે
    val colorCode = items[position].colorCode
    
    holder.tvEnglishDate.text = engDate
    
    if (engDate.isEmpty()) {
        holder.tvTithi.text = ""
        return
    }

    val localInfo = data.get(selectedLang)?.asString ?: ""
    holder.tvTithi.text = localInfo

    if (colorCode == 1) {
        holder.tvEnglishDate.setTextColor(Color.RED)
        holder.tvTithi.setTextColor(Color.RED)
    } else {
        holder.tvEnglishDate.setTextColor(Color.BLACK)
        holder.tvTithi.setTextColor(Color.DKGRAY)
    }
}
