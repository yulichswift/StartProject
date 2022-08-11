package com.jeff.startproject.ui.draw

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jeff.startproject.R
import com.log.JFLog

@SuppressLint("CustomViewStyleable")
class DrawTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var textRect = Rect()
    private val refRect = Rect()
    private var scoreMargin = 0f
    private var dp18 = 0f
    private var w = 0f
    private var h = 0f
    private var score1 = ""
    private var score2 = ""

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextAppearance)
        val attrsColor = typedArray.getColor(R.styleable.TextAppearance_android_textColor, 0)
        val attrsSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0f)
        val attrsFont = typedArray.getResourceId(R.styleable.TextAppearance_android_fontFamily, 0)
        typedArray.recycle()

        val density = context.resources.displayMetrics.density

        scoreMargin = 6 * density
        dp18 = 18f * density


        paint.apply {
            color = attrsColor
            textSize = dp18
            isAntiAlias = true

            /*
            if (attrsFont != 0) {
                typeface = ResourcesCompat.getFont(context, attrsFont)
            }
            */
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = resolveSizeAndState(0, widthMeasureSpec, 1)
        val h = resolveSizeAndState(0, heightMeasureSpec, 0)

        JFLog.d("W: $w, H: $h.")
        // setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()

        paint.getTextBounds("0", 0, 1, refRect)

        updateRect()
    }

    private fun updateRect() {
        if (text.isNotEmpty()) {
            paint.getTextBounds(text.toString(), 0, text.length, textRect)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        JFLog.d("onDraw")

        if (score1.isNotEmpty() && score2.isNotEmpty()) {
            canvas?.drawScore()
        } else if (text.isNotEmpty()) {
            canvas?.drawCenter()
        }
    }

    private fun Canvas.drawScore() {
        paint.textAlign = Paint.Align.CENTER
        drawText(":", w / 2f, h / 2f + refRect.height() / 2f, paint)

        paint.textAlign = Paint.Align.RIGHT
        drawText(score1, w / 2f - scoreMargin, h / 2f + refRect.height() / 2f, paint)

        paint.textAlign = Paint.Align.LEFT
        drawText(score2, w / 2f + scoreMargin, h / 2f + refRect.height() / 2f, paint)
    }

    private fun Canvas.drawCenter() {
        paint.textAlign = Paint.Align.CENTER
        drawText(text.toString(), w / 2f, h / 2f + textRect.height() / 2f, paint)
    }

    fun setScore(s1: String = score1, s2: String = score2) {
        if (score1 != s1 || score2 != s2) {
            score1 = s1
            score2 = s2

            invalidate()
        }
    }
}
