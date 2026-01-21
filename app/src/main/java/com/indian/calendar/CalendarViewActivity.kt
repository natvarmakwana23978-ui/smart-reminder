// API Response મળ્યા પછી [cite: 2026-01-21]
if (response.isSuccessful && !response.body().isNullOrEmpty()) {
    val firstDay = response.body()!![0].englishDate // દા.ત. "Thu Jan 01 2026" [cite: 2026-01-21]
    val parts = firstDay?.split(" ")
    if (parts != null && parts.size >= 4) {
        val monthYear = "${parts[1]} ${parts[3]}" // Jan 2026 [cite: 2026-01-21]
        findViewById<TextView>(R.id.tvMonthYearLabel).text = monthYear
    }
    recyclerView.adapter = CalendarAdapter(response.body()!!)
}
