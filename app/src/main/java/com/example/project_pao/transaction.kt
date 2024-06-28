package com.example.project_pao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class transaction(
    @Expose
    @SerializedName("transaction_id") val transaction_id: String,

    @Expose
    @SerializedName("amount") val amount: Int,

    @Expose
    @SerializedName("note") val note: String,

    @Expose
    @SerializedName("wallet_id") val wallet_id: String,

    @Expose
    @SerializedName("category_id") val category_id: String,

    @Expose
    @SerializedName("type_id") val type_id: String,

    @Expose
    @SerializedName("createAt") val createAt: String,

) {}