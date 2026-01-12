package com.indian.calendar

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // આખી લિંકને બદલે માત્ર 'macros/...' વાળો ભાગ જ લખવો
    @GET("macros/s/AKfycbwDwYZJb_nPjjqoUI28-7FNksbv6o766zWjeRhewouc0vvnNJXj8s4EkTnuWWxh4U7l/exec")
    fun getCalendars(): Call<List<CalendarModel>>
}
