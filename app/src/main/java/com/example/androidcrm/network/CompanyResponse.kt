package com.example.androidcrm.network

import com.google.gson.annotations.SerializedName

data class CompanyResponse(
    @SerializedName("business_name")
    val name: String,

    @SerializedName("industry")
    val company: String,

    @SerializedName("phone_number")
    val phone: String
)