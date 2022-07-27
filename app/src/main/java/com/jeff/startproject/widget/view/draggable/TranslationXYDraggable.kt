package com.jeff.startproject.widget.view.draggable

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import com.jeff.startproject.MyApplication

class TranslationXYDraggable : BaseViewDraggable() {
    private var latestRawX = 0f
    private var latestRawY = 0f

    private var downX = 0f
    private var downY = 0f
    private val clickedDiffValue by lazy { targetView?.get()?.resources?.displayMetrics?.density ?: 1f }

    private var _rangeX = 0f
    private val rangeX: Float
        get() {
            if (_rangeX == 0f) {
                _rangeX = targetView?.get()?.let {
                    MyApplication.getScreenWidthPx() - it.measuredWidth
                }?.toFloat() ?: 0f
            }

            return _rangeX
        }

    private var _rangeY = 0f
    private val rangeY: Float
        get() {
            if (_rangeY == 0f) {
                _rangeY = targetView?.get()?.let {
                    MyApplication.getScreenHeightPx() - it.measuredHeight
                }?.toFloat() ?: 0f
            }

            return _rangeY
        }

    override fun removeTarget() {
        super.removeTarget()

        _rangeX = 0f
        _rangeY = 0f
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                latestRawX = event.rawX
                downY = event.y
                latestRawY = event.rawY
            }

            MotionEvent.ACTION_MOVE -> {
                val rawX = event.rawX
                val rawY = event.rawY

                if (latestRawX != rawX || latestRawY != rawY) {
                    val moveX = latestRawX - rawX
                    latestRawX = rawX

                    val moveY = latestRawY - rawY
                    latestRawY = rawY

                    moveLocation(moveX, moveY)
                }
            }

            MotionEvent.ACTION_UP -> {
                // 如果移動位置, 就攔截觸摸事件, 不讓點擊事件生效.
                return event.x !in (downX - clickedDiffValue)..(downX + clickedDiffValue) || event.y !in (downY - clickedDiffValue)..(downY + clickedDiffValue)
            }
        }

        return true
    }

    override fun moveLocation(x: Int, y: Int) {
        targetView?.get()?.apply {
            if (x != 0 && rangeX < 0) {
                (translationX - x).also {
                    translationX = when {
                        it > 0 -> 0f
                        it < rangeX -> rangeX
                        else -> it
                    }
                }
            }

            if (y != 0 && rangeY < 0) {
                (translationY - y).also {
                    translationY = when {
                        it > 0 -> 0f
                        it < rangeY -> rangeY
                        else -> it
                    }
                }
            }
        }
    }
}
