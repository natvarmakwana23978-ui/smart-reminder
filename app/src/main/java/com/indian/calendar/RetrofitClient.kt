object RetrofitClient {
    private const val BASE_URL = "https://script.google.com/macros/s/AKfycbzsFfM_jo2P_PmCtDyccoC6KIubETZxjAnAtwLBTJRtidKIicS5cKf9l5KrMC9TDRWt/"

    private val okHttp = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
