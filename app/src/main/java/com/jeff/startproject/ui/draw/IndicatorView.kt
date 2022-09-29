package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.log.JFLog

class IndicatorView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val density = resources.displayMetrics.density
        circleRadius = 6f * density
        distance = 12f * density

        val bgColor = Color.WHITE
        val circleShadowColor = Color.parseColor("#4D4C9EEA")
        val circleShadowRadius = 10f * density

        unselectedPaint.color = bgColor
        unselectedPaint.setShadowLayer(circleShadowRadius, 0f, 0f, circleShadowColor)

        val selectedColor = Color.parseColor("#4C9EEA")
        selectedPaint.color = selectedColor
    }

    private var w = 0f
    private var h = 0f
    private var circleRadius = 0f
    private var distance = 0f
    private val unselectedPaint = Paint()
    private val selectedPaint = Paint()

    var count = 8
        set(value) {
            field = value
            invalidate()
        }

    var selectedIndex = 0
        set(value) {
            field = value
            invalidate()
        }

    // 需要重新計算大小時，才需要覆寫 onMeasure，否則使用 onSizeChanged。
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val mW = (circleRadius * 2f) * count + distance * (count - 1f)

        val w = resolveSizeAndState(mW.toInt(), widthMeasureSpec, 0)
        val h = resolveSizeAndState((circleRadius * 2f).toInt(), heightMeasureSpec, 0)

        JFLog.d("W: $w, H: $h.")

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        if (count > 0 && w > 0f) {
            canvas?.also {
                repeat(count) { i ->
                    val paint = if (i == selectedIndex) selectedPaint else unselectedPaint
                    it.drawCircle(circleRadius + (circleRadius * 2f + distance) * i, h / 2f, circleRadius, paint)
                }
            }
        }
    }
}
