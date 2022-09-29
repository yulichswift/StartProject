package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ShadowView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val shadowLineColor = Color.parseColor("#05000000")
        val shadowColor = Color.parseColor("#33000000")
        val shadowRadius = 2f

        // setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint)
        leftShadowPaint.color = shadowLineColor
        leftShadowPaint.style = Paint.Style.STROKE
        leftShadowPaint.setShadowLayer(shadowRadius, -shadowRadius + 1f, 0f, shadowColor)
        // paint.maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.OUTER)

        rightShadowPaint.color = shadowLineColor
        rightShadowPaint.style = Paint.Style.STROKE
        rightShadowPaint.setShadowLayer(shadowRadius, shadowRadius - 1f, 0f, shadowColor)


        val circleColor = Color.WHITE
        val circleShadowColor = Color.parseColor("#4D4C9EEA")
        val circleShadowRadius = 10f * resources.displayMetrics.density

        circlePaint.color = circleColor
        circlePaint.setShadowLayer(circleShadowRadius, 0f, 0f, circleShadowColor)
    }

    private var w = 0f
    private var h = 0f
    private val leftShadowPaint = Paint()
    private val rightShadowPaint = Paint()
    private val circlePaint = Paint()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.also {
            val paddingX = 0f
            it.drawLine(paddingX, 0f, paddingX, w, leftShadowPaint)
            it.drawLine(w - paddingX, 0f, w - paddingX, h, rightShadowPaint)

            it.drawCircle(w / 2f, h / 2f, 100f, circlePaint)
        }
    }
}
