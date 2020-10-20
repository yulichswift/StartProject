package com.jeff.startproject.floating.draggable

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

class MovingDraggable : BaseDraggable() {
    private var latestRawX = 0f
    private var latestRawY = 0f

    // private var downX = 0f
    // private var downY = 0f
    // private val clickedDiffValue by lazy { targetView?.get()?.resources?.displayMetrics?.density ?: 1f }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // downX = event.x
                // downY = event.y
                latestRawX = event.rawX
                latestRawY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                if (!canMove) return false

                val rawX = event.rawX
                val rawY = event.rawY

                if (latestRawX != rawX || latestRawY != rawY) {
                    val moveX = latestRawX - rawX
                    val moveY = latestRawY - rawY
                    latestRawX = rawX
                    latestRawY = rawY

                    moveLocation(moveX, moveY)
                }
            }

            MotionEvent.ACTION_UP -> {
                canMove = false
                return false
                // 如果沒有設置 LongClickListener
                // 如果移動位置, 就攔截觸摸事件, 不讓點擊事件生效.
                // return event.x !in (downX - clickedDiffValue)..(downX + clickedDiffValue)
                //        || event.y !in (downY - clickedDiffValue)..(downY + clickedDiffValue)
            }
        }
        return false
    }
}
