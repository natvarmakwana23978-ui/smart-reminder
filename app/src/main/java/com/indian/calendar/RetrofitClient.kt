package com.indian.calendar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbEBQpPWdz_7SjzjghU4IaMBIaR98LOrop8qeGhBXmlfog028zg6TfdujX0RQzehUDH/"

    // ગૂગલના રીડાયરેક્ટ્સને હેન્ડલ કરવા માટે ક્લાયન્ટ
    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // આ ક્લાયન્ટ ઉમેરવો જરૂરી છે
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}
