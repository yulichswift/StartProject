package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.log.JFLog

data class LineObject(
    val x1: Float,
    val y1: Float,
    val x2: Float,
    val y2: Float,
) {
    var isSelected: Boolean = false

    fun onDraw(canvas: Canvas, paint: Paint) {
        canvas.drawLine(x1, y1, x2, y2, paint)
    }
}

data class HintObject(
    val rectF: RectF,
    val isRightSlash: Boolean,
    val relationList: List<LineObject>,
) {
    fun isHint(x: Float, y: Float): Boolean = rectF.contains(x, y)

    fun updateSelected(x: Float, y: Float) {
        if (isHint(x, y)) {
            relationList.forEach { it.isSelected = true }
        }
    }
}

class DrawView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val linePaint = Paint()
    private val selectedLinePaint = Paint()
    private val rectPaint = Paint()
    private val textRectPaint = Paint()
    private val text8Paint = Paint()
    private val text12Paint = Paint()
    private val lineList = mutableListOf<LineObject>()
    private val hintList = mutableListOf<HintObject>()
    private var w = 0f
    private var h = 0f
    private var radius = 0f
    private var showRightSlashLive = true
    private val rightSlashTextRect = Rect()
    private val leftSlashTextRect = Rect()
    private val tempSlashTextRect = RectF()
    private var dp3 = 0f
    private var dp5 = 0f
    private var dp8 = 0f
    private var dp12 = 0f
    private var text1 = "LIVE"
    private var text2 = "1:4"

    init {
        val density = context.resources.displayMetrics.density

        radius = 24f * density

        dp3 = 3f * density
        dp5 = 5f * density
        dp8 = 8f * density
        dp12 = 12f * density

        linePaint.apply {
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 1f * density
            isAntiAlias = true
        }

        selectedLinePaint.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 1f * density
            isAntiAlias = true
        }

        rectPaint.apply {
            color = Color.argb(128, 127, 0, 127)
            style = Paint.Style.FILL
            strokeWidth = 1f * density
            isAntiAlias = true
        }

        textRectPaint.apply {
            color = Color.YELLOW
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 1f * density
            isAntiAlias = true
        }

        text8Paint.apply {
            color = Color.BLUE
            textAlign = Paint.Align.CENTER
            textSize = dp8
            isAntiAlias = true
        }

        text12Paint.apply {
            color = Color.BLUE
            textAlign = Paint.Align.CENTER
            textSize = dp12
            isAntiAlias = true
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.also { motionEvent ->
            var isActionMove = false
            var isActionDown = false

            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_CANCEL -> JFLog.d("ACTION_CANCEL")
                MotionEvent.ACTION_DOWN -> {
                    isActionDown = true
                    JFLog.d("ACTION_DOWN")
                }
                MotionEvent.ACTION_MOVE -> {
                    isActionMove = true
                    // JFLog.d("ACTION_MOVE")
                }
                MotionEvent.ACTION_OUTSIDE -> JFLog.d("ACTION_OUTSIDE")
                MotionEvent.ACTION_UP -> JFLog.d("ACTION_UP")
            }

            if (isActionMove.not()) {
                JFLog.d("x: ${motionEvent.x}, y: ${motionEvent.y}")
                JFLog.d("rawX: ${motionEvent.rawX}, rawY: ${motionEvent.rawY}")
            }

            if (isActionDown) {
                var isHint = false
                for (it in hintList) {
                    if (it.isHint(motionEvent.x, motionEvent.y)) {
                        isHint = true
                        break
                    }
                }

                if (isHint) {
                    resetSelected()

                    hintList.forEach {
                        it.updateSelected(motionEvent.x, motionEvent.y)
                        if (it.isHint(motionEvent.x, motionEvent.y)) {
                            showRightSlashLive = it.isRightSlash
                        }
                    }

                    invalidate()
                }
            }
        }

        // true 才能夠偵測後續狀態, 但會攔截事件.
        return true
        // return super.onTouchEvent(event)
    }

    // 需要重新計算大小時，才需要覆寫 onMeasure，否則使用 onSizeChanged。
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = resolveSizeAndState(0, widthMeasureSpec, 1)
        // 高度為WrapContent時: h = w
        val h = resolveSizeAndState(w, heightMeasureSpec, 0)

        JFLog.d("W: $w, H: $h.")

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()

        initSize()
        initList()
    }

    override fun onDraw(canvas: Canvas?) {
        JFLog.d("onDraw")

        super.onDraw(canvas)

        canvas?.apply {
            lineList.forEach {
                it.onDraw(canvas, if (it.isSelected) selectedLinePaint else linePaint)
            }

            hintList.forEach {
                // drawRect(it.rectF, rectPaint)
                val rect = it.rectF
                drawCircle(rect.right - radius, rect.bottom - radius, radius, rectPaint)
            }

            if (showRightSlashLive) {
                save()

                translate(w / 2, h / 2)
                rotate(45f)

                // 畫右斜線文字外框
                tempSlashTextRect.set(rightSlashTextRect)
                tempSlashTextRect.inset(-dp3, -dp3)
                drawRect(tempSlashTextRect, textRectPaint)
                drawRoundRect(tempSlashTextRect, dp5, dp5, selectedLinePaint)

                // 畫右斜線文字
                drawText(text1, 0f, rightSlashTextRect.height() / 2f, text8Paint)

                restore()
            } else {
                save()

                translate(w / 2, h / 2)
                rotate(-45f)

                // 畫左斜線文字外框
                tempSlashTextRect.set(leftSlashTextRect)
                tempSlashTextRect.inset(-dp3, -dp3)
                drawRect(tempSlashTextRect, textRectPaint)
                drawRoundRect(tempSlashTextRect, dp5, dp5, selectedLinePaint)

                // 畫左斜線文字
                drawText(text1, 0f, rightSlashTextRect.height() / 2f, text8Paint)

                restore()
            }

            // 垂直文字
            text2.forEachIndexed { i, c ->
                drawText(c.toString(), dp12, radius * 2f + dp12 * (i + 1), text12Paint)
            }
        }
    }

    private fun initSize() {
        // 右文字外框
        text8Paint.getTextBounds(text1, 0, text1.length, rightSlashTextRect)
        rightSlashTextRect.offset(-rightSlashTextRect.width() / 2, rightSlashTextRect.height() / 2)

        // 左文字外框
        text8Paint.getTextBounds(text1, 0, text1.length, leftSlashTextRect)
        leftSlashTextRect.offset(-leftSlashTextRect.width() / 2, leftSlashTextRect.height() / 2)
    }

    private fun initList() {
        lineList.clear()
        hintList.clear()

        // left
        val left = LineObject(radius, radius, radius, h - radius)
        lineList.add(left)

        // top
        val top = LineObject(radius, radius, w - radius, radius)
        lineList.add(top)

        // right
        val right = LineObject(w - radius, radius, w - radius, h - radius)
        lineList.add(right)

        // bottom
        val bottom = LineObject(radius, h - radius, w - radius, h - radius)
        lineList.add(bottom)

        // right slash
        val rightSlash = LineObject(radius, radius, w - radius, h - radius)
        lineList.add(rightSlash)

        // left slash
        val leftSlash = LineObject(w - radius, radius, radius, h - radius)
        lineList.add(leftSlash)

        val rectSize = radius * 2f
        hintList.add(HintObject(rectF = RectF(0f, 0f, rectSize, rectSize), isRightSlash = true, relationList = listOf(left, top, rightSlash)))
        hintList.add(HintObject(rectF = RectF(w - rectSize, 0f, w, rectSize), isRightSlash = false, relationList = listOf(right, top, leftSlash)))
        hintList.add(HintObject(rectF = RectF(0f, h - rectSize, rectSize, h), isRightSlash = false, relationList = listOf(left, bottom, leftSlash)))
        hintList.add(HintObject(rectF = RectF(w - rectSize, h - rectSize, w, h), isRightSlash = true, relationList = listOf(right, bottom, rightSlash)))
    }

    private fun resetSelected() {
        lineList.forEach { it.isSelected = false }
    }
}
