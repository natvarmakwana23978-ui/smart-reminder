package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // BASE_URL હંમેશા સ્લેશ (/) થી પૂરી થવી જોઈએ અને તેમાં 'exec' ન હોવું જોઈએ
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbzHOrbkFzwuFut-nIKgodBGji_DLE0LQda3qeH5Ml0oVXdQdOg18tUG9CtmCX8nBYF7/exec" 

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
