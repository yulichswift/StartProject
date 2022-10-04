package com.jeff.startproject.ui.clip

import android.content.Context
import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.cos
import kotlin.math.sin

class PolygonConstraintLayout : ConstraintLayout {
    private var w = 0f
    private var h = 0f
    private val clipPath = Path()

    private val maxState = 16

    var state = 1
        private set

    var angleOffset = 0f
        set(value) {
            if (field != value) {
                field = value

                updatedPath()
                invalidateOutline()
            }
        }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        clipToOutline = true
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View?, outline: Outline?) {
                if (view != null && outline != null && !clipPath.isEmpty) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        outline.setPath(clipPath)
                    } else {
                        outline.setConvexPath(clipPath)
                    }
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.w = w.toFloat()
        this.h = h.toFloat()

        updatedPath()
    }

    private fun updatedPath() {
        clipPath.reset()

        when (state) {
            // 圓形
            1 -> clipPath.addCircle(w / 2f, h / 2f, w / 2f, Path.Direction.CW)
            in 3..maxState -> {
                val r = w / 2f
                val originOffset = w / 2f
                val angle = (2f * Math.PI / state).toFloat()

                // 起始點
                clipPath.moveTo(cos(angleOffset) * r + originOffset, sin(angleOffset) * r + originOffset)

                for (i in 1..state) {
                    val a = angle * i + angleOffset
                    clipPath.lineTo(cos(a) * r + originOffset, sin(a) * r + originOffset)
                }

                clipPath.close()
            }
        }
    }

    fun nextState() {
        when (state) {
            1 -> {
                state = 3
                updatedPath()

                clipToOutline = true

                invalidateOutline()
            }
            maxState -> {
                state = 0

                clipToOutline = false
            }
            else -> {
                state++
                updatedPath()

                clipToOutline = true

                invalidateOutline()
            }
        }
    }

    /** 無法運作時, 必須啟用程式碼.
    override fun draw(canvas: Canvas) {
    val save = canvas.save()
    canvas.clipPath(clipPath)
    super.draw(canvas)
    canvas.restoreToCount(save)
    }

    override fun dispatchDraw(canvas: Canvas) {
    val save = canvas.save()
    canvas.clipPath(clipPath)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }
    */
}
