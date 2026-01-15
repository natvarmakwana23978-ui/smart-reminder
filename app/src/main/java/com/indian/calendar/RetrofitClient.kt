package com.indian.calendar

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    // 🔹 Google Script EXEC URL માંથી /exec કાઢીને
    private const val BASE_URL =
        "https://script.google.com/macros/s/AKfycbzsFfM_jo2P_PmCtDyccoC6KIubETZxjAnAtwLBTJRtidKIicS5cKf9l5KrMC9TDRWt/"

    private val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
