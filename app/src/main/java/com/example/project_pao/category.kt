package com.example.project_pao

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class category(
    @Expose
    @SerializedName("category_id") val id: String,

    @Expose
    @SerializedName("category_name") val name: String,


    @Expose
    @SerializedName("category_icon") val icon: String,

    @Expose
    @SerializedName("type_id") val type_id: String


) {}