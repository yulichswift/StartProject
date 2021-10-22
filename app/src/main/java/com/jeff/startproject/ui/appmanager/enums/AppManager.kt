package com.jeff.startproject.ui.appmanager.enums

import android.content.pm.ApplicationInfo

enum class AppType {
    All,
    NonSystem,
    System,
    Recent,
    ;

    companion object {
        fun typeWithAppInfo(appInfo: ApplicationInfo): AppType {
            return if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) == 1) {
                System
            } else {
                NonSystem
            }
        }
    }
}

enum class IconType {
    Logo,
    Icon,
    Unbadged,
    Banner,
    ;
}

enum class ViewType {
    Loading,
    Normal,
}