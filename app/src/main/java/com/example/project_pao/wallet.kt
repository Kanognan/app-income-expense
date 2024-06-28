package com.example.project_pao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class wallet(
    @Expose
    @SerializedName("wallet_id") val wallet_id: String,

    @Expose
    @SerializedName("wallet_name") val wallet_name: String,

    @Expose
    @SerializedName("balance") val balance: Int,

    @Expose
    @SerializedName("us_id") val wallet_us_id: String,

    @Expose
    @SerializedName("currentcy") val currentcy: String,

    @Expose
    @SerializedName("all_income") val all_income: Int,

    @Expose
    @SerializedName("all_expent") val all_expent: Int
) {}