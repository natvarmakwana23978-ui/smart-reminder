package com.smart.reminder

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("exec")
    fun getCalendarData(): Call<List<CalendarDayData>>
}
