package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    /**
     * 1️⃣ બધા ઉપલબ્ધ કેલેન્ડર લિસ્ટ માટે
     * (Google Sheet → App Script → JSON Array)
     */
    @GET("macros/s/AKfycbzsFfM_jo2P_PmCtDyccoC6KIubETZxjAnAtwLBTJRtidKIicS5cKf9l5KrMC9TDRWt/exec")
    fun getCalendars(): Call<List<CalendarModel>>

    /**
     * 2️⃣ યુઝર દ્વારા પસંદ કરેલ કેલેન્ડર મુજબનો ડેટા
     * colIndex = Google Sheet નો column index
     */
    @GET("macros/s/AKfycbzsFfM_jo2P_PmCtDyccoC6KIubETZxjAnAtwLBTJRtidKIicS5cKf9l5KrMC9TDRWt/exec")
    fun getCalendarData(
        @Query("colIndex") colIndex: Int
    ): Call<List<CalendarDayData>>
}
