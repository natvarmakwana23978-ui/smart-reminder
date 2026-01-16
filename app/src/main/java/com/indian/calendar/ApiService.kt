package com.indian.calendar

import com.indian.calendar.model.CalendarDayData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("exec")
    fun getCalendars(): Call<List<CalendarItem>>

    @GET("exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
