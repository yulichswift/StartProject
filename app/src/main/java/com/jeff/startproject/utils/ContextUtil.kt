package com.jeff.startproject.utils

import android.content.Context
import android.os.LocaleList
import java.util.*

object ContextUtil {
    fun getContextLocale(context: Context): Locale = context.resources.configuration.locales[0]!!

    fun updateContext(context: Context, locale: Locale) {
        context.resources.apply {
            configuration.apply {
                setLocales(LocaleList(locale))
                setLayoutDirection(locale)
            }

            updateConfiguration(configuration, displayMetrics)
        }
    }
}