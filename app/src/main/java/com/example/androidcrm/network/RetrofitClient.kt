package com.example.androidcrm.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * A singleton object to provide a configured Retrofit instance.
 * This ensures we use the same object for all API calls, which is efficient.
 */
object RetrofitClient {

    private const val BASE_URL = "https://random-data-api.com/"

    // Use 'lazy' to create the Retrofit instance only when it's first needed.
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson to parse JSON
            .build()

        retrofit.create(ApiService::class.java)
    }
}