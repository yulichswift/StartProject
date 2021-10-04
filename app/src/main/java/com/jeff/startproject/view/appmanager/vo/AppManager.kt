package com.jeff.startproject.view.appmanager.vo

import android.content.pm.ApplicationInfo
import com.jeff.startproject.view.appmanager.enums.AppType
import com.jeff.startproject.view.appmanager.enums.ViewType

data class AppViewData(
    val appType: AppType = AppType.All,
    val appInfo: ApplicationInfo? = null,
    val viewType: ViewType = ViewType.Normal,
)

data class PassAppInfo(
    val appType: AppType,
    val packageName: String,
)