package com.jeff.startproject.widget.floating.draggable

import android.view.Gravity
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import java.lang.ref.WeakReference

abstract class BaseDraggable : OnTouchListener {
    protected var targetView: WeakReference<View>? = null
        private set
    protected var windowManager: WindowManager? = null
        private set
    protected var windowParams: WindowManager.LayoutParams? = null
        private set

    protected var canMove = false

    fun start(windowManager: WindowManager, view: View, layoutParams: WindowManager.LayoutParams) {
        this.targetView = WeakReference(view)
        this.windowManager = windowManager
        this.windowParams = layoutParams

        view.setOnTouchListener(this)
        view.setOnLongClickListener {
            canMove = true
            true
        }
    }

    fun stop() {
        targetView = null
        windowManager = null
        windowParams = null
    }

    protected fun moveLocation(x: Float, y: Float) {
        moveLocation(x.toInt(), y.toInt())
    }

    protected fun moveLocation(x: Int, y: Int) {
        if (x == 0 && y == 0) return

        targetView?.get()?.also { view ->
            windowManager?.also { windowManager ->
                windowParams?.also { windowParams ->
                    windowParams.x -= x
                    windowParams.y -= y
                    // 設置重心位置為左上
                    windowParams.gravity = Gravity.TOP or Gravity.START
                    windowManager.updateViewLayout(view, windowParams)
                }
            }
        }
    }

    protected fun updateLocation(x: Float, y: Float) {
        updateLocation(x.toInt(), y.toInt())
    }

    protected fun updateLocation(x: Int, y: Int) {
        targetView?.get()?.also { view ->
            windowManager?.also { windowManager ->
                windowParams?.also { windowParams ->
                    if (windowParams.x != x || windowParams.y != y) {
                        windowParams.x = x
                        windowParams.y = y
                        // 設置重心位置為左上
                        windowParams.gravity = Gravity.TOP or Gravity.START
                        windowManager.updateViewLayout(view, windowParams)
                    }
                }
            }
        }
    }
}
