package com.jeff.startproject.view.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout

class RoundFrameLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    private var cornerRadius = 0f

    private val rectF by lazy {
        RectF(0f, 0f, 0f, 0f)
    }

    private val path by lazy {
        Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        cornerRadius = w / 2f

        rectF.apply {
            right = w.toFloat()
            bottom = h.toFloat()
        }

        path.reset()
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
    }

    /*
    override fun draw(canvas: Canvas?) {
        val save = canvas?.save()
        canvas?.clipPath(path)
        super.draw(canvas)
        save?.let(canvas::restoreToCount)
    }
    */

    override fun dispatchDraw(canvas: Canvas?) {
        val save = canvas?.save()
        canvas?.clipPath(path)
        super.dispatchDraw(canvas)
        save?.let(canvas::restoreToCount)
    }
}