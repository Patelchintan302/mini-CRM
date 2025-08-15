package com.example.androidcrm.network

import com.google.gson.annotations.SerializedName

// The top-level response object contains a list of results
data class UserResponse(
    @SerializedName("results")
    val results: List<UserResult>
)

// This class holds the actual user data we need
data class UserResult(
    @SerializedName("name")
    val name: Name,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String
)

// A nested class for the user's first and last name
data class Name(
    @SerializedName("first")
    val first: String,

    @SerializedName("last")
    val last: String
)
