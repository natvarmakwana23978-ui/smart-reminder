package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    // @Url વાપરવાથી આપણે ડાયરેક્ટ આખી લિંક મોકલી શકીશું
    @GET("https://script.google.com/macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/exec")
    fun getCalendars(): Call<List<CalendarModel>>

    // બીજા ફંક્શનમાં પણ આખું URL જ વાપરીએ (ફક્ત colIndex પેરામીટર સાથે)
    @GET("https://script.google.com/macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/exec")
    fun getCalendarData(@Query("colIndex") colIndex: Int): Call<List<CalendarDayData>>
}
