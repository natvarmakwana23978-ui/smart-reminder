package com.indian.calendar

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("exec")
    fun getCalendarData(
        @Query("sheetName") sheetName: String,
        @Query("action") action: String
    ): Call<JsonObject>
}
