package com.jeff.startproject.view.appmanager

import android.content.pm.ApplicationInfo

data class CustomApplicationInfo(
    val appType: AppManagerViewModel.AppType,
    val appInfo: ApplicationInfo,
)

data class PassAppInfo(
    val appType: AppManagerViewModel.AppType,
    val packageName: String,
)