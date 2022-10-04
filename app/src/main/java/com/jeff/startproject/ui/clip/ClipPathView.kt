package com.jeff.startproject.ui.clip

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ClipPathView : View {
    private var w = 0f
    private var h = 0f
    private val paint = Paint()
    private val circlePaint = Paint()
    private val circleShadowRadius: Float

    private val rect = RectF()
    private val clipPath = Path()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val density = resources.displayMetrics.density

        paint.apply {
            color = Color.LTGRAY
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = density
            isAntiAlias = true
        }

        val circleColor = Color.TRANSPARENT
        val circleShadowColor = Color.parseColor("#4D4C9EEA")
        circleShadowRadius = 10f * resources.displayMetrics.density

        circlePaint.color = circleColor
        circlePaint.setShadowLayer(circleShadowRadius, 0f, 0f, circleShadowColor)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()

        rect.set(0f, 0f, this.w, this.h)

        clipPath.reset()
        clipPath.addCircle(w / 2f, h / 2f, w / 2f, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            clipPath(clipPath)

            drawRect(rect, paint)

            drawCircle(w / 2f, h / 2f, w / 2f - circleShadowRadius, circlePaint)
        }
    }
}
