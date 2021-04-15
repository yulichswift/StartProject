package com.jeff.startproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.jeff.startproject.databinding.View3Binding
import com.log.JFLog

class CustomView3 @kotlin.jvm.JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        var inCacheCount = 0
    }

    private val binding = View3Binding.inflate(LayoutInflater.from(context), this, true)
    val btn by lazy(binding::btn)
    private val index: Int = inCacheCount

    init {
        inCacheCount += 1
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // JFLog.d("onAttachedToWindow")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        // JFLog.d("onDetachedFromWindow")
    }

    protected fun finalize() {
        inCacheCount -= 1
        JFLog.d("GC $index cache: $inCacheCount")
    }
}