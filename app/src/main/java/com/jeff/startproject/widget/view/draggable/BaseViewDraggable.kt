package com.jeff.startproject.widget.view.draggable

import android.view.View
import android.view.View.OnTouchListener
import java.lang.ref.WeakReference

abstract class BaseViewDraggable : OnTouchListener {
    protected var targetView: WeakReference<View>? = null
        private set

    fun setTargetView(view: View) {
        this.targetView = WeakReference(view)

        targetView?.get()?.setOnTouchListener(this)
    }

    open fun removeTarget() {
        targetView = null
    }

    protected open fun moveLocation(x: Float, y: Float) {
        moveLocation(x.toInt(), y.toInt())
    }

    protected open fun moveLocation(x: Int, y: Int) {
        if (x == 0 && y == 0) return

        targetView?.get()?.apply {
            translationX -= x
            translationY -= y
        }
    }
}
