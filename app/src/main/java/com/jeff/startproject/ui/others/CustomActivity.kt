package com.jeff.startproject.ui.others

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.jeff.startproject.MyApplication
import com.jeff.startproject.databinding.ActivityCustomBinding
import com.jeff.startproject.widget.CustomView3
import com.ui.base.BaseActivity

class CustomActivity : BaseActivity<ActivityCustomBinding>() {

    override fun getViewBinding(): ActivityCustomBinding {
        return ActivityCustomBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.byDecorView.setOnClickListener {
            addCustomViewByDecorView(it)
        }

        binding.byWindowManager.setOnClickListener {
            addCustomViewByWindowManager(it)
        }
    }

    /**
     * DecorView適用於只會在當下Window顯示的View.
     * View如果在不同Window間顯示需要透過WindowManager, WindowManager在管理上較方便.
     */
    private fun addCustomViewByDecorView(view: View) {
        val rect = Rect().also { view.getGlobalVisibleRect(it) }
        val lp = FrameLayout.LayoutParams(rect.width(), rect.height()).apply {
            setMargins(rect.left, rect.top, 0, 0)
        }

        val customView = CustomView3(this)
        (window.decorView as ViewGroup).apply {
            addView(customView, lp)

            customView.btn.setOnClickListener {
                removeView(customView)
            }
        }
    }

    private fun addCustomViewByWindowManager(view: View) {
        val rect = Rect().also { view.getGlobalVisibleRect(it) }
        val lp = WindowManager.LayoutParams().apply {
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START
            width = rect.width()
            height = rect.height()
            x = rect.left
            // WindowManager會從StatusBarHeight計算位置
            // GlobalVisibleRect時納入StatusBarHeight
            y = rect.top - MyApplication.getStatusBarHeight()
        }

        val customView = CustomView3(this)

        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        windowManager.addView(customView, lp)

        customView.btn.setOnClickListener {
            windowManager.removeView(customView)
        }
    }
}