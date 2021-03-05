package com.jeff.startproject.view.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class ArcView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var cornerSize = 0f
    private val paint = Paint()

    init {
        paint.color = Color.YELLOW
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

        cornerSize = w / 2f
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
        path.moveTo(0f, cornerSize)
        path.lineTo(0f, 0f)
        path.lineTo(cornerSize, 0f)
        rectF.apply {
            left = 0f
            top = 0f
            right = cornerSize * 2
            bottom = cornerSize * 2
        }
        path.arcTo(rectF, -90f, -90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawLeftBottom(canvas: Canvas) {
        path.reset()
        path.moveTo(0f, height - cornerSize)
        path.lineTo(0f, height.toFloat())
        path.lineTo(cornerSize, height.toFloat())
        rectF.apply {
            left = 0f
            top = height - cornerSize * 2
            right = cornerSize * 2
            bottom = height.toFloat()
        }
        path.arcTo(rectF, 90f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawRightBottom(canvas: Canvas) {
        path.reset()
        path.moveTo(width.toFloat() - cornerSize, height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), height - cornerSize)
        rectF.apply {
            left = width - cornerSize * 2
            top = height - cornerSize * 2
            right = width.toFloat()
            bottom = height.toFloat()
        }
        path.arcTo(rectF, 0f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }

    private fun drawRightTop(canvas: Canvas) {
        path.reset()
        path.moveTo(width.toFloat(), cornerSize)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo(width.toFloat() - cornerSize, 0f)
        rectF.apply {
            left = width - cornerSize * 2
            top = 0f
            right = width.toFloat()
            bottom = cornerSize * 2
        }
        path.arcTo(rectF, -90f, 90f)
        path.close()
        canvas.drawPath(path, paint)
    }
}