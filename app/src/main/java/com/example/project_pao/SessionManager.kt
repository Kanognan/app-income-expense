package com.example.project_pao

import android.content.Context
import android.content.SharedPreferences

class SessionManager {
    var pref: SharedPreferences
    var edior: SharedPreferences.Editor
    var context: Context
    var PRIVATE_MODE: Int = 0

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        edior = pref.edit()
    }

    companion object {
        val PREF_NAME: String = "SessionDemo"
        val IS_LOGIN: String = "isLogin"
        val KEY_ID: String = "id"
        val KEY_EMAIL: String = "email"
        val KEY_WALLET: String = "wallet_id"
    }

    fun createLoginSession(id: String, email: String , wallet_id:String) {
        edior.putBoolean(IS_LOGIN, true)
        edior.putString(KEY_ID, id)
        edior.putString(KEY_EMAIL, email)
        edior.putString(KEY_WALLET,wallet_id)
        edior.commit()
    }

    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }
}