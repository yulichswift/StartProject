package com.jeff.startproject.ui.appmanager.vo

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import com.jeff.startproject.ui.appmanager.enums.AppType
import com.jeff.startproject.ui.appmanager.enums.ViewType

data class AppViewData(
    val appType: AppType = AppType.All,
    val appInfo: ApplicationInfo? = null,
    val pkgInfo: PackageInfo? = null,
    val viewType: ViewType = ViewType.Normal,
)

data class PassAppInfo(
    val appType: AppType,
    val packageName: String,
)