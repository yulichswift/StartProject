package com.jeff.startproject.model.pref

import com.google.gson.Gson
import com.jeff.startproject.model.pref.token.TokenData

class Pref(private val gson: Gson, preferenceFileName: String) :
    AbstractPref(preferenceFileName) {

    private val tokenPref = StringPref("TOKEN")

    var token: TokenData
        get() =
            try {
                gson.fromJson(tokenPref.get(), TokenData::class.java)
            } catch (e: Exception) {
                TokenData()
            }
        set(value) {
            tokenPref.set(gson.toJson(value))
        }

    fun clearToken() {
        tokenPref.remove()
    }
}