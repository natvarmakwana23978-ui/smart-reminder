override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val day = days[position]
    holder.txtDate.text = day.Date
    
    // જે કોલમ સિલેક્ટ કરી હોય તેની તિથિ બતાવશે
    holder.txtTithi.text = when(colIndex) {
        1 -> day.Gujarati
        2 -> day.Hindi
        else -> day.Gujarati
    }

    // જો ગૂગલ શીટમાં 'Warning' વાળા ખાનામાં લખાણ હોય તો જ લાલ પટ્ટી બતાવવી
    // તમારી શીટ મુજબ અહીં લોજિક સેટ કરવું
    holder.txtAlertBanner.visibility = View.GONE 
}
