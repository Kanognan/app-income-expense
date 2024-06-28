package com.example.project_pao


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*

interface userApi {

    @GET("user")
    fun getuser(
        @Field("us_email") us_email: String,
        @Field("us_password") us_password: String
    ): retrofit2.Call<user>

    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("us_email") us_email: String,
        @Field("us_password") us_password: String
    ): retrofit2.Call<user>

    @FormUrlEncoded

    @POST("user")
    fun insertuser(
        @Field("us_email") us_email: String,
        @Field("us_password") us_password: String
    ): retrofit2.Call<user>

    @POST("wallet")
    @FormUrlEncoded
    fun insertwallet(
        @Field("us_id") wallet_us_id: String
    ): retrofit2.Call<wallet>


    @POST("transaction")
    @FormUrlEncoded
    fun inserttransaction(
        @Field("wallet_id")  wallet_id: String,
        @Field("category_id")  category_id: String,
        @Field("type_id") type_id: String,
        @Field("createAt")  createAt: String,
        @Field("amount")  amount: Int,
        @Field("note")  note: String

    ): retrofit2.Call<transaction>


    @FormUrlEncoded
    @PUT("wallet/{us_id}")
    fun updateWallet(
        @Path("us_id") us_id: String,
        @Field("currentcy") currentcy: String,
        @Field("wallet_name") wallet_name: String
    ): retrofit2.Call<wallet>

    @FormUrlEncoded
    @PUT("wallet/{us_id}")
    fun updateWallet2(
        @Path("us_id") us_id: String,
        @Field("balance")  balance: Int,
        @Field("all_income") all_income: Int,
        @Field("all_expent") all_expent: Int
    ): retrofit2.Call<wallet>

    @GET("allcategory/{category_id}") /// Call NodeJS
    fun retrieveCategory(
        @Path("category_id") category_id: String
    ): retrofit2.Call<List<category>>

    @GET("wallet/{us_id}")
    fun getwalletID(
        @Path("us_id") us_id:String
    ): retrofit2.Call<wallet>


    @GET("allwallet/{us_id}")
    fun getwallet(
        @Path("us_id") us_id: String
    ): retrofit2.Call<wallet>



    @GET("getalltransaction/{wallet_id}")
    fun gettransaction(
        @Path("wallet_id") wallet_id: String
    ):retrofit2.Call<List<transaction>>

    @GET("gettransactionid/{transaction_id}")
    fun gettransactionid(
        @Path("transaction_id") transaction_id: String
    ):retrofit2.Call<transaction>

    @GET("alluser/{us_id}")
    fun retrieveProfile(
        @Path("us_id") us_id: String
    ):retrofit2.Call<user>

    @FormUrlEncoded
    @PUT("user/{us_id}")
    fun updatePassword(
        @Path("us_id") us_id: Int,
        @Field("us_email") us_email: String,
        @Field("us_password") us_password: String,
        @Field("us_profile") us_profile: String
    ):retrofit2.Call<user>

//    @GET("salltransaction") /// Call NodeJS
//    fun retrievetran(
//    ):retrofit2.Call<List<transactionClass>>

    @GET("tran/{wallet_id}")
    fun retrievetran(
        @Path("wallet_id") wallet_id: String
    ):retrofit2.Call<List<transactionClass>>

    @GET("userpie/{us_id}")
    fun retrieveWalletID(
        @Path("us_id") wallet_us_id: String
    ):retrofit2.Call<wallet>


    @FormUrlEncoded
    @PUT("updatetran/{transaction_id}")
    fun updatetran(
        @Path("transaction_id") transaction_id:String,
        @Field("createAt")  createAt: String,
        @Field("amount")  amount: Int,
        @Field("note")  note: String
    ):retrofit2.Call<transaction>

    @DELETE("trandel/{transaction_id}")
    fun deletetran(
        @Path("transaction_id") transaction_id:String
    ):retrofit2.Call<transaction>




    companion object {
        fun create(): userApi {
            val userClient: userApi = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(userApi::class.java)
            return userClient

        }
    }
}