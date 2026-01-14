package com.indian.calendar

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // તમારી નવી લિંક
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbyEBQpPWdz_7SjzjghU4IaMBlaR98LORop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // અહીં ::class.java વાપરવું ફરજિયાત છે
        retrofit.create(ApiService::class.java)
    }
}
