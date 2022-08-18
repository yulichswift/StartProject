package com.jeff.startproject.ui.draw

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.text.StaticLayout
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.jeff.startproject.R
import com.log.JFLog

@SuppressLint("CustomViewStyleable")
class DrawTextView2 @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatTextView(context, attrs) {

    private var staticLayout: StaticLayout? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextAppearance)
        val attrsColor = typedArray.getColor(R.styleable.TextAppearance_android_textColor, 0)
        val attrsSize = typedArray.getDimension(R.styleable.TextAppearance_android_textSize, 0f)
        val attrsFont = typedArray.getResourceId(R.styleable.TextAppearance_android_fontFamily, 0)
        typedArray.recycle()

        val density = context.resources.displayMetrics.density

        paint.apply {
            color = attrsColor
            // textSize = attrsSize
            isAntiAlias = true

            /*
            if (attrsFont != 0) {
                typeface = ResourcesCompat.getFont(context, attrsFont)
            }
            */
        }

        text = "Canvas.drawText不會自動換行，超出螢幕部分會看不見。 StaticLayout是負責文字的類別，可以處理文字換行。"
    }

    // 需要重新計算大小時，才需要覆寫 onMeasure，否則使用 onSizeChanged。
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = resolveSizeAndState(0, widthMeasureSpec, 1)

        staticLayout = StaticLayout.Builder.obtain(text, 0, text.length, paint, w).build()
        val h = staticLayout?.height ?: 0

        resolveSizeAndState(h, heightMeasureSpec, 0)

        JFLog.d("W: $w, H: $h.")

        setMeasuredDimension(w, h)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas?) {
        staticLayout?.draw(canvas)
    }
}
