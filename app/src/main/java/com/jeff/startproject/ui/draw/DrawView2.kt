package com.jeff.startproject.ui.draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.log.JFLog

class DrawView2 : View {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val density = context.resources.displayMetrics.density

        linePaint.apply {
            color = Color.YELLOW
            style = Paint.Style.STROKE
            strokeWidth = 3f * density
            isAntiAlias = true
        }

        selectedLinePaint.apply {
            color = Color.LTGRAY
            style = Paint.Style.STROKE
            strokeWidth = 1f * density
            isAntiAlias = true
        }

        pointPaint.apply {
            color = Color.RED
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 3f * density
            isAntiAlias = true
        }

        trianglePaint.apply {
            color = Color.BLUE
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = density
            isAntiAlias = true
            // 圓角
            pathEffect = CornerPathEffect(10 * density)
        }
    }

    private val linePaint = Paint()
    private val selectedLinePaint = Paint()
    private val pointPaint = Paint()
    private val trianglePaint = Paint()
    private var w = 0f
    private var h = 0f
    private var currentDegrees = 0f
    private val aroundPath = Path()
    private val trianglePath = Path()

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
        }

        // true 才能夠偵測後續狀態
        return true
        // return super.onTouchEvent(event)
    }

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

        initPath()
    }

    override fun onDraw(canvas: Canvas?) {
        JFLog.d("onDraw")

        canvas?.apply {
            fun toDegrees(to: Float) {
                if (currentDegrees == to) return

                rotate(to - currentDegrees)
                currentDegrees = to
            }

            drawPath(aroundPath, linePaint)

            canvas.save()
            canvas.translate(w / 2f, h / 2f)
            drawPath(trianglePath, trianglePaint)
            canvas.restore()

            val pointList = listOf(
                PointF(0f, 0f),
                PointF(200f, 25f),
                PointF(100f, 475f),
                PointF(300f, 500f),
            )

            Path().let {
                it.moveTo(pointList[0].x, pointList[0].y)
                it.cubicTo(pointList[1].x, pointList[1].y, pointList[2].x, pointList[2].y, pointList[3].x, pointList[3].y)
                drawPath(it, selectedLinePaint)
            }

            for (point in pointList) {
                drawPoint(point.x, point.y, pointPaint)
            }
        }
    }

    private fun initPath() {
        aroundPath.also {
            it.reset()
            it.moveTo(0f, 0f)
            it.lineTo(w, 0f)
            it.lineTo(w, h)
            it.lineTo(0f, h)
            it.lineTo(0f, 0f)
        }

        val dp = resources.displayMetrics.density
        val l = 50 * dp

        trianglePath.also {
            it.reset()
            it.moveTo(0f, 0f)
            it.lineTo(l, 0f)
            it.lineTo(l / 2f, l)
            it.close()
        }
    }
}
