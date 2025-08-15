package com.example.androidcrm.network

import com.example.androidcrm.network.CompanyResponse
import retrofit2.Call
import retrofit2.http.GET

/**
 * This interface defines the API endpoints.
 * Retrofit will use this to generate the necessary networking code.
 */
interface ApiService {
    /**
     * Fetches a single random company.
     * The @GET annotation specifies the path relative to the base URL.
     */
    @GET("api/company/random_company")
    fun getRandomCompany(): Call<CompanyResponse>
}