package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jeff.startproject.R

class RoundView : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundView)
        val backgroundColor = typedArray.getColor(R.styleable.RoundView_viewColor, Color.TRANSPARENT)
        cornerRadius = typedArray.getDimension(R.styleable.RoundView_cornerRadius, 0f)
        typedArray.recycle()

        paint.color = backgroundColor
        paint.isAntiAlias = true
    }

    private var cornerRadius = 0f
        set(value) {
            field = value
            cornerArray = floatArrayOf(value, value, value, value, value, value, value, value)
        }

    private val paint = Paint()

    private val rectF by lazy {
        RectF(0f, 0f, width.toFloat(), height.toFloat())
    }

    private val path by lazy {
        Path()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (cornerRadius == 0f) {
            cornerRadius = w / 2f
        }
    }

    private lateinit var cornerArray: FloatArray

    override fun onDraw(canvas: Canvas?) {
        canvas?.also {
            path.reset()
            path.addRoundRect(rectF, cornerArray, Path.Direction.CW)
            it.clipPath(path)
            drawRectF(it)
        }
    }

    private fun drawRectF(canvas: Canvas) {
        path.reset()
        path.addRect(rectF, Path.Direction.CW)
        canvas.drawPath(path, paint)
    }
}
