package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // મુખ્ય લિસ્ટ મેળવવા માટે [cite: 2026-01-21]
    @GET("exec")
    fun getCalendarList(): Call<List<CalendarItem>>

    // કેલેન્ડરના દિવસો મેળવવા માટે [cite: 2026-01-21]
    @GET("exec")
    fun getCalendarData(
        @Query("action") action: String = "getCalendarDays",
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
