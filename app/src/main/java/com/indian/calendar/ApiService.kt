package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // ૧. બધા ઉપલબ્ધ કેલેન્ડર (Sheets) નું લિસ્ટ મેળવવા માટે
    @GET("exec")
    fun getCalendarList(): Call<CalendarListResponse>

    // ૨. સિલેક્ટ કરેલા કેલેન્ડરનો બધો જ ડેટા (બધી ભાષાઓ સાથે) મેળવવા માટે
    @GET("exec")
    fun getCalendarData(
        @Query("calendar") calendarName: String
    ): Call<List<Map<String, String>>> 
    // અહીં Map એટલે વાપર્યું છે કારણ કે તમારી શીટમાં ઘણી બધી ભાષાઓ (Columns) છે.
}
