package com.indian.calendar

import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // કેલેન્ડર લિસ્ટ મેળવવા માટે (Selection Activity માટે)
    @GET("exec")
    fun getCalendars(@Query("action") action: String = "getCalendars"): Call<List<CalendarItem>>

    // જે-તે ભાષાનો ડેટા મેળવવા માટે (View Activity માટે)
    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int,
        @Query("action") action: String = "getCalendarData"
    ): Call<List<CalendarDayData>>
}
