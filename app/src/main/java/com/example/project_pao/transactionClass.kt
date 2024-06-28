package com.example.project_pao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class transactionClass(
    @Expose
    @SerializedName("transaction_id") val transaction_id: Int,

    @Expose
    @SerializedName("amount") val amount: Int,

    @Expose
    @SerializedName("createAt") val createAt: String,

    @Expose
    @SerializedName("type_id") val type_id: String,

    @Expose
    @SerializedName("category_id") val category_id: String,

    @Expose
    @SerializedName("category_name") val category_name: String,

    @Expose
    @SerializedName("category_icon") val category_icon: String,

//    @Expose
//    @SerializedName("wallet_id") val wallet_id: String,

    @Expose
    @SerializedName("note") val note: String
) {}