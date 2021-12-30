package com.jeff.startproject.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jeff.startproject.ui.appmanager.AppManagerActivity

@Deprecated("no used", replaceWith = ReplaceWith("androidx.core:core-splashscreen"))
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, AppManagerActivity::class.java))
        finish()
    }
}