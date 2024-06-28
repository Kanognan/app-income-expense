package com.example.project_pao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class user(
    @Expose
    @SerializedName("us_id") val us_id: String,

    @Expose
    @SerializedName("us_email") val us_email: String,

    @Expose
    @SerializedName("us_password") val us_password: String,

    @Expose
    @SerializedName("us_profile") val image: String

) {}