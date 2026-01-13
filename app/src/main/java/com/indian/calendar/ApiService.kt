package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // ૨૭ કેલેન્ડરનું લિસ્ટ મેળવવા માટે
    @GET("macros/s/તમારી_સ્ક્રિપ્ટ_આઈડી/exec")
    fun getCalendars(): Call<List<CalendarModel>>

    // પસંદ કરેલ કેલેન્ડરનો ડેટા મેળવવા માટે
    @GET("macros/s/તમારી_સ્ક્રિપ્ટ_આઈડી/exec")
    fun getCalendarData(@Query("colIndex") colIndex: Int): Call<List<CalendarDayData>>
}
