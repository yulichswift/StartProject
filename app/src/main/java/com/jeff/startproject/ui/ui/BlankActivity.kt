package com.jeff.startproject.ui.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import com.google.android.material.button.MaterialButton
import com.log.JFLog

class BlankActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // NavigationBar 設置透明:
        // windowTranslucentNavigation = true 還是有遮罩.
        // windowTranslucentNavigation = false, navigationBarColor = Color.TRANSPARENT.
        window.navigationBarColor = Color.TRANSPARENT

        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        super.onCreate(savedInstanceState)

        val lp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.CENTER
        }

        val finishBtn1 = MaterialButton(this).apply {
            text = "Finish (decorView)"
            alpha = 0.75f
            setOnClickListener {
                it.visibility = View.GONE
            }
        }
        (window.decorView as ViewGroup).addView(finishBtn1, lp)

        val finishBtn2 = MaterialButton(this).apply {
            text = "Finish (content)"
            alpha = 0.75f
            setOnClickListener {
                finish()
            }
        }
        findViewById<ViewGroup>(android.R.id.content).addView(finishBtn2, lp)

        printForEach((window.decorView as ViewGroup))
    }

    private fun printForEach(viewGroup: ViewGroup, level: Int = 0) {
        val step = "  "

        var prefix = ""
        repeat(level) {
            prefix += step
        }

        JFLog.d("$prefix$level: $viewGroup")

        val childPrefix = prefix + step
        val nextLevel = level + 1
        viewGroup.forEach {
            if (it is ViewGroup) {
                printForEach(it, nextLevel)
            } else {
                JFLog.d("$childPrefix$nextLevel: $it")
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
