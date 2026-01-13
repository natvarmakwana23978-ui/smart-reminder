package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // Apps Script માંથી ડેટા મેળવવા માટેની મેથડ
    @GET("exec")
    fun getCalendarData(@Query("colIndex") colIndex: Int): Call<List<CalendarDayData>>
}
