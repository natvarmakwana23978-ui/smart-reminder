package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી Apps Script ને Deploy કર્યા પછી મળતી લિંક અહીં 'exec' પહેલા સુધીની નાખો
    private const val BASE_URL = "https://script.google.com/macros/s/તમારી_સ્ક્રિપ્ટ_આઈડી_અહીં_નાખો/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
