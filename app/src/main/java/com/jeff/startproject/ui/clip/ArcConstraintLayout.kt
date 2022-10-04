package com.jeff.startproject.ui.clip

import android.content.Context
import android.graphics.Outline
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewOutlineProvider
import androidx.constraintlayout.widget.ConstraintLayout

class ArcConstraintLayout : ConstraintLayout {
    private var w = 0f
    private var h = 0f
    private val clipPath = Path()
    private val arcCount = 10
    private var arcLength = 0f
    private var arcRadius = 0f
    private var space = 0f

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

        val ratio = 2.5f
        space = this.h / (arcCount * ratio + arcCount + 1)
        arcLength = space * ratio
        arcRadius = arcLength / 2f

        updatedPath()
    }

    private fun updatedPath() {
        clipPath.reset()

        clipPath.moveTo(0f, 0f)
        repeat(arcCount) { i ->
            val dY = i * (space + arcLength)
            clipPath.lineTo(0f, dY + space)
            clipPath.arcTo(-arcRadius, dY + space, arcRadius, dY + space + arcLength, 270f, 180f, false)
        }
        clipPath.lineTo(0f, h)
        clipPath.lineTo(w, h)

        clipPath.moveTo(0f, 0f)
        clipPath.lineTo(w, 0f)
        repeat(arcCount) { i ->
            val dY = i * (space + arcLength)
            clipPath.lineTo(w, dY + space)
            clipPath.arcTo(w - arcRadius, dY + space, w + arcRadius, dY + space + arcLength, 270f, -180f, false)
        }
        clipPath.lineTo(w, h)

        clipPath.close()
    }
}
