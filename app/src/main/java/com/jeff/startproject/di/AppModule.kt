package com.jeff.startproject.di

import com.google.gson.Gson
import com.jeff.startproject.Constant
import com.jeff.startproject.model.pref.Pref
import org.koin.dsl.module

val appModule = module {
    single { provideGson()}
    single { providePref(get()) }
}

fun provideGson() : Gson = Gson()

fun providePref(gson: Gson): Pref {
    return Pref(gson, Constant.PREFS_NAME)
}
