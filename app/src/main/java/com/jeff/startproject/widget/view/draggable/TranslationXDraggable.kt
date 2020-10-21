package com.jeff.startproject.widget.view.draggable

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import com.jeff.startproject.MyApplication
import kotlin.math.absoluteValue

class TranslationXDraggable : BaseViewDraggable() {
    private var latestRawX = 0f

    private var downX = 0f
    private val clickedDiffValue by lazy { targetView?.get()?.resources?.displayMetrics?.density ?: 1f }

    private val screenWidth by lazy { MyApplication.getScreenWidthPx() }
    private val targetViewWidth by lazy {
        val rect = Rect()
        targetView?.get()?.getGlobalVisibleRect(rect)
        rect.width()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                latestRawX = event.rawX
            }

            MotionEvent.ACTION_MOVE -> {
                val rawX = event.rawX

                if (latestRawX != rawX) {
                    val moveX = latestRawX - rawX
                    latestRawX = rawX

                    moveLocation(moveX, 0f)
                }
            }

            MotionEvent.ACTION_UP -> {
                val rangeXOfMove = screenWidth - targetViewWidth
                val finalX =
                    if (latestRawX < rangeXOfMove / 2) {
                        // 移動到最左側
                        0
                    } else {
                        // 移動到最右側
                        rangeXOfMove
                    }

                startAnimation(finalX.toFloat())
                // 如果移動位置, 就攔截觸摸事件, 不讓點擊事件生效.
                return event.x !in (downX - clickedDiffValue)..(downX + clickedDiffValue)
            }
        }
        return false
    }

    private fun startAnimation(toX: Float) {
        targetView?.get()?.also { view ->
            val originX = view.translationX
            val animator = ValueAnimator.ofFloat(originX, toX)
            animator.duration = ((toX - originX).absoluteValue / 2).toLong()
            animator.addUpdateListener { animation -> updateLocation(animation.animatedValue as Float) }
            animator.start()
        }
    }

    private fun updateLocation(x: Float) {
        targetView?.get()?.also { view ->
            if (view.translationX != x) {
                view.translationX = x
            }
        }
    }
}
