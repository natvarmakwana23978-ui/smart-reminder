package indian.calendar

// ... અન્ય જરૂરી imports ...

private fun setupCalendarData(responseBody: List<JsonObject>, selectedCalendarName: String) {
    val daysList = mutableListOf<CalendarDayData>()

    for (jsonObj in responseBody) {
        val dayData = CalendarDayData(
            englishDate = jsonObj.get("ENGLISH")?.asString ?: "",
            allData = jsonObj // આખી રો સાચવી લીધી
        )
        daysList.add(dayData)
    }

    // 'selectedCalendarName' માં યુઝરે પસંદ કરેલ કોઈપણ કેલેન્ડરનું હેડર નામ હશે [cite: 2026-01-07]
    // જેમ કે "હિજરી", "વિક્રમ સંવત", "શક સંવત" વગેરે.
    val adapter = CalendarAdapter(daysList, selectedCalendarName)
    recyclerView.adapter = adapter
}
