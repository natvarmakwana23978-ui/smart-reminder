val adapter = MonthPagerAdapter(
    context = this,
    startCalendar = Calendar.getInstance()
) { calendar: Calendar ->

    val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    val keyMonth = sdf.format(calendar.time)

    sheetDataMap
        .filterKeys { key -> key.startsWith(keyMonth) }
        .map { entry ->
            CalendarData(
                englishDate = entry.key,
                tithi = entry.value.tithi,
                festival = entry.value.festival
            )
        }
}
