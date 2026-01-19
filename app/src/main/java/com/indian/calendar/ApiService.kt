package com.indian.calendar

import com.indian.calendar.model.CalendarDayData
import com.indian.calendar.model.CalendarItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // કેલેન્ડરનું લિસ્ટ મેળવવા માટે
    @GET("exec")
    fun getCalendars(@Query("action") action: String = "getCalendars"): Call<List<CalendarItem>>

    // જે-તે ભાષાનો ડેટા (૨૬ ભાષા) મેળવવા માટે [cite: 2026-01-07]
    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int,
        @Query("action") action: String = "getCalendarData"
    ): Call<List<CalendarDayData>>
}
