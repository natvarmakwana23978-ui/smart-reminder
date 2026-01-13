package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbxDIcaJQ85VDhuup3Qn2YUCpnhX1OtDDTEIgMkTyqVeKHGmSBlh2-hukwhU6L58r1UO/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
