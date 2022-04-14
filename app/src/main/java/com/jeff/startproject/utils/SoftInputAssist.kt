package com.jeff.startproject.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class SoftInputAssist {
    /**
     * Adjust layout on immersive mode
     */
    constructor(activity: Activity, lifecycle: Lifecycle, adjustView: View) {
        val contentAreaOfWindowBounds = Rect()
        var areaHeightMax = 0
        var areaHeightPrevious = 0

        val content = activity.findViewById<ViewGroup>(android.R.id.content)

        val globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            content.getWindowVisibleDisplayFrame(contentAreaOfWindowBounds)
            val areaHeightNow: Int = contentAreaOfWindowBounds.height()
            if (areaHeightMax == 0 || areaHeightNow > areaHeightMax)
                areaHeightMax = areaHeightNow

            if (areaHeightPrevious != areaHeightNow) {
                areaHeightPrevious = areaHeightNow
                when {
                    areaHeightMax != areaHeightNow -> areaHeightMax - areaHeightNow
                    else -> 0
                }.let {
                    adjustView.setPadding(0, 0, 0, it)
                }
            }
        }

        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                content.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
            }

            override fun onPause(owner: LifecycleOwner) {
                content.viewTreeObserver.removeOnGlobalLayoutListener(globalLayoutListener)
                adjustView.setPadding(0, 0, 0, 0)
            }
        })
    }
}
