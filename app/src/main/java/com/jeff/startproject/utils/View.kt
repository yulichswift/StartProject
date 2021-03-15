package com.jeff.startproject.utils

import android.view.View
import android.view.ViewGroup
import android.view.WindowManager

fun View.findDecorView(): ViewGroup? {
    var view = this
    while (view.parent is View) {
        view = view.parent as View
        if (view.layoutParams is WindowManager.LayoutParams) {
            return view as? ViewGroup
        }
    }
    return null
}
