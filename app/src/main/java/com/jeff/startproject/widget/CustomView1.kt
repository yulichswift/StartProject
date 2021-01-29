package com.jeff.startproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.children
import com.jeff.startproject.databinding.View1Binding
import com.log.JFLog

class CustomView1 @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {

    private val binding0 = View1Binding.inflate(LayoutInflater.from(context), this, true)
    private val binding1 = View1Binding.inflate(LayoutInflater.from(context), this, true)

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        JFLog.d("[onAttachedToWindow] rootView: $rootView")
        JFLog.d("[onAttachedToWindow] this: $this")

        this.children.forEachIndexed { index, view ->
            JFLog.d("[onAttachedToWindow] this.children[$index]: $view")
        }

        JFLog.d("[onAttachedToWindow] binding0.root: ${binding0.root}")
        JFLog.d("[onAttachedToWindow] binding1.root: ${binding1.root}")

        binding0.textView.text = "Jeff"
    }
}