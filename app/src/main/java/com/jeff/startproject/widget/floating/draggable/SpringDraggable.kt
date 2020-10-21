package com.jeff.startproject.widget.floating.draggable

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View

class SpringDraggable : BaseDraggable() {
    private var latestRawX = 0f
    private var latestRawY = 0f

    // protected var downX = 0f
    // protected var downY = 0f
    // private val clickedDiffValue by lazy { targetView?.get()?.resources?.displayMetrics?.density ?: 1f }

    private val targetViewWidth by lazy {
        val rect = Rect()
        targetView?.get()?.getGlobalVisibleRect(rect)
        rect.width()
    }

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
                if (canMove) {
                    val rangeXOfMove = screenWidth - targetViewWidth
                    val finalX =
                        if (windowParams!!.x < rangeXOfMove / 2) {
                            // 移動到最左側
                            0
                        } else {
                            // 移動到最右側
                            rangeXOfMove
                        }

                    startAnimation(finalX)
                }

                canMove = false
                return false
                // 如果沒有設置 LongClickListener
                // 如果移動位置, 就攔截觸摸事件, 不讓點擊事件生效.
                // return !(event.x in (downX - clickedDiffValue)..(downX + clickedDiffValue)
                //        && event.y in (downY - clickedDiffValue)..(downY + clickedDiffValue))
            }
        }
        return false
    }

    /**
     * 獲取螢幕的寬度
     */
    private val screenWidth: Int
        get() {
            val outMetrics = DisplayMetrics()
            windowManager?.defaultDisplay?.getMetrics(outMetrics)
            return outMetrics.widthPixels
        }

    private fun startAnimation(toX: Int) {
        val animator = ValueAnimator.ofInt(windowParams!!.x, toX)
        animator.duration = 300
        animator.addUpdateListener { animation -> updateLocation(animation.animatedValue as Int, windowParams!!.y) }
        animator.start()
    }
}
