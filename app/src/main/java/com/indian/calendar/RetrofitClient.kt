package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // લિંકને મેં અહીં સુધારી દીધી છે (છેલ્લે સ્લેશ ઉમેર્યો છે)
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbxuy1payoK1f4NKgxWV38Yu9N6SxzVYb6Mg-EEWCU3tztye4kV9R6Ipn-W62Eq6e6rr/" 

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
