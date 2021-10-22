package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.jeff.startproject.R

class ArcView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var cornerRadius = 0f
    private val paint = Paint()

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArcView)
        val viewColor = typedArray.getColor(R.styleable.ArcView_viewColor, Color.TRANSPARENT)
        cornerRadius = typedArray.getDimension(R.styleable.ArcView_cornerRadius, 0f)
        typedArray.recycle()

        paint.color = viewColor
        paint.isAntiAlias = true
    }

    private val path by lazy {
        Path()
    }

    private val rectF by lazy {
        RectF(0f, 0f, 0f, 0f)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (cornerRadius == 0f) {
            cornerRadius = w / 2f
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.also {
            drawLeftTop(canvas)
            drawRightTop(canvas)
            drawLeftBottom(canvas)
            drawRightBottom(canvas)
        }
    }

    private fun drawLeftTop(canvas: Canvas) {
        path.reset()
        path.moveTo(0f, cornerRadius)
        path.lineTo(0f, 0f)
        path.lineTo(cornerRadius, 0f)
        rectF.apply {
            left = 0f
            top = 0f
            right = cornerRadius * 2
            bottom = cornerRadius * 2
        }
        path.arcTo(rectF, -90f, -90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawLeftBottom(canvas: Canvas) {
        path.reset()
        path.moveTo(0f, height - cornerRadius)
        path.lineTo(0f, height.toFloat())
        path.lineTo(cornerRadius, height.toFloat())
        rectF.apply {
            left = 0f
            top = height - cornerRadius * 2
            right = cornerRadius * 2
            bottom = height.toFloat()
        }
        path.arcTo(rectF, 90f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawRightBottom(canvas: Canvas) {
        path.reset()
        path.moveTo(width.toFloat() - cornerRadius, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), height - cornerRadius)
        rectF.apply {
            left = width - cornerRadius * 2
            top = height - cornerRadius * 2
            right = width.toFloat()
            bottom = height.toFloat()
        }
        path.arcTo(rectF, 0f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawRightTop(canvas: Canvas) {
        path.reset()
        path.moveTo(width.toFloat(), cornerRadius)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat() - cornerRadius, 0f)
        rectF.apply {
            left = width - cornerRadius * 2
            top = 0f
            right = width.toFloat()
            bottom = cornerRadius * 2
        }
        path.arcTo(rectF, -90f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }
}