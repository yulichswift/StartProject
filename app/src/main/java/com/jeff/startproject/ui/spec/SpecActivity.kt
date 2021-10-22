package com.jeff.startproject.ui.spec

import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import com.jeff.startproject.MyApplication
import com.jeff.startproject.databinding.ActivitySpecBinding
import com.log.JFLog
import com.ui.base.BaseActivity

class SpecActivity : BaseActivity<ActivitySpecBinding>() {
    override fun getViewBinding(): ActivitySpecBinding {
        return ActivitySpecBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        JFLog.d("MANUFACTURER: ${Build.MANUFACTURER}")
        JFLog.d("MODEL: ${Build.MODEL}")

        // heightPixels 根據手機型號不同, 有些包含status bar高度, 有些會扣掉.
        JFLog.d("heightPixels: ${resources.displayMetrics.heightPixels}")

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        JFLog.d("metrics heightPixels: ${metrics.heightPixels}")

        val realMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(realMetrics)
        JFLog.d("realMetrics heightPixels: ${realMetrics.heightPixels}")

        JFLog.d("statusBar height: ${MyApplication.getStatusBarHeight()}")
        JFLog.d("bottomNavigationBar height: ${MyApplication.getBottomNavigationBarHeight()}")
    }
}