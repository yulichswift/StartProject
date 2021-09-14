package com.jeff.startproject.di

import com.google.gson.Gson
import com.jeff.startproject.Constant
import com.jeff.startproject.model.pref.Pref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun providePref(gson: Gson): Pref {
        return Pref(gson, Constant.PREFS_NAME)
    }
}
