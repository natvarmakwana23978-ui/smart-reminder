interface ApiService {
    @GET("exec")
    suspend fun getCalendars(): List<CalendarItem>

    @GET("exec")
    suspend fun getCalendarData(@Query("colIndex") colIndex: Int): List<CalendarData>
}

data class CalendarItem(val calendarName: String, val creatorName: String)
data class CalendarData(val englishDate: String, val tithi: String?, val festival: String?)
