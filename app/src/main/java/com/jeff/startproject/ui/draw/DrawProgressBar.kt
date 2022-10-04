package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class DrawProgressBar : View {
    private var w = 0f
    private var h = 0f
    private val progressPaint = Paint()
    private val bgPaint = Paint()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val density = context.resources.displayMetrics.density

        progressPaint.apply {
            color = Color.parseColor("#B70047")
            style = Paint.Style.FILL
            isAntiAlias = true
            pathEffect = CornerPathEffect(2f * density)
        }

        bgPaint.apply {
            color = Color.parseColor("#FFB500")
            style = Paint.Style.FILL
            isAntiAlias = true
            pathEffect = CornerPathEffect(2f * density)
        }
    }

    var progress: Float = 0f
        set(value) {
            if (field != value) {
                field = value
                invalidate()
            }
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        if (w == 0f || h == 0f) return

        canvas?.apply {
            val midX = w * progress

            if (midX < w) {
                drawRect(0f, 0f, w, h, bgPaint)
            }

            if (midX > 0f) {
                drawRect(0f, 0f, midX, h, progressPaint)
            }
        }
    }
}
