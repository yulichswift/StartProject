package com.jeff.startproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.children
import com.jeff.startproject.R
import com.log.JFLog

class CustomView2 @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater.from(context).inflate(R.layout.view1, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        JFLog.d("[onAttachedToWindow] rootView: $rootView")
        JFLog.d("[onAttachedToWindow] this: $this")

        this.children.forEachIndexed { index, view ->
            JFLog.d("[onAttachedToWindow] this.children[$index]: $view")
        }

        val view = findViewById<View>(R.id.layout)
        JFLog.d("[onAttachedToWindow] view: $view")
    }
}