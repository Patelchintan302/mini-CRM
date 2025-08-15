package com.example.androidcrm.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    // UPDATED: New endpoint and response type
    @GET("api/")
    fun getRandomUser(): Call<UserResponse>
}