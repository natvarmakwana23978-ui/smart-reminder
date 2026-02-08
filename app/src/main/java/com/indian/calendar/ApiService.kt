package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exec")
    fun getCalendarData(
        @Query("calendar") sheetName: String,
        @Query("action") action: String
    ): Call<List<CalendarDayData>> // અહીં List વાપરવાથી પેલી Type Mismatch એરર જતી રહેશે
}
