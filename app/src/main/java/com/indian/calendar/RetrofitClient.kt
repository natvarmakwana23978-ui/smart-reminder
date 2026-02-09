package com.smart.reminder

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // લિંકને મેં અહીં સુધારી દીધી છે (છેલ્લે સ્લેશ ઉમેર્યો છે)
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxgAC_Vg3d7c5P5iyieEmqSGM1AqGjW3tXC36wk1rx4yJR0NuE8_Bc90W0NyTadxcA/" 

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
