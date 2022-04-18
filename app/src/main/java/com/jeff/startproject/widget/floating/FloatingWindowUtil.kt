package com.jeff.startproject.widget.floating

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.widget.floating.draggable.BaseDraggable
import com.jeff.startproject.widget.view.draggable.TranslationXDraggable

object FloatingWindowUtil {

    // WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE: 視窗以外的區域會接收到事件
    fun getEnableTouchFlag() = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

    // WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE: 視窗不會接收事件
    fun getDisableTouchFlag() = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE

    // 全螢幕
    fun getFullScreenFlag() = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS

    fun createDraggableWindow(context: Context, draggable: BaseDraggable) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layoutParams = WindowManager.LayoutParams().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START
            width = (MyApplication.getScreenWidthPx() * .85f).toInt()
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = (MyApplication.getScreenWidthPx() * .075f).toInt()
            y = 0
        }

        val floatingLayout = inflater.inflate(R.layout.view_floating, null)
        windowManager.addView(floatingLayout, layoutParams)

        draggable.start(windowManager, floatingLayout, layoutParams)

        floatingLayout.findViewById<View>(R.id.btn).setOnClickListener {
            draggable.stop()
            windowManager.removeView(floatingLayout)
        }
    }

    fun createHorizontalDraggableWindow(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val layoutParams = WindowManager.LayoutParams().apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = 0
            y = (MyApplication.getScreenHeightPx() * .5f).toInt()
        }

        val floatingLayout = inflater.inflate(R.layout.view_floating, null)

        windowManager.addView(floatingLayout, layoutParams)

        val draggable = TranslationXDraggable()

        draggable.setTargetView(floatingLayout)

        floatingLayout.findViewById<View>(R.id.btn).setOnClickListener {
            draggable.removeTarget()
            windowManager.removeView(floatingLayout)
        }
    }

    fun createDraggableWindow(
        context: Context,
        isFullScreen: Boolean = false,
        view: View,
        draggable: BaseDraggable,
    ): () -> Unit {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams().apply {
            type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            flags = if (isFullScreen) {
                getEnableTouchFlag() or getFullScreenFlag()
            } else {
                getEnableTouchFlag()
            }
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            x = 0
            y = 0
        }

        windowManager.addView(view, layoutParams)

        draggable.start(windowManager, view, layoutParams)

        return { draggable.stop() }
    }

    fun createFloatingWindow(
        context: Context,
        isGlobal: Boolean = true,
        isFullScreen: Boolean = false,
        view: View,
        widthPercent: Float? = null,
        heightPercent: Float? = null,
        initX: Float? = null,
        initY: Float? = null,
    ) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams = WindowManager.LayoutParams().apply {
            if (isGlobal) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                }
            } else {
                type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL
            }
            flags = if (isFullScreen) {
                getEnableTouchFlag() or getFullScreenFlag()
            } else {
                getEnableTouchFlag()
            }
            format = PixelFormat.TRANSLUCENT
            gravity = Gravity.TOP or Gravity.START

            val screenWidth = MyApplication.getScreenWidthPx()
            val screenHeight = if (isFullScreen) {
                MyApplication.getFullScreenHeightPx()
            } else {
                MyApplication.getScreenHeightPx()
            }

            width = when (widthPercent) {
                null -> WindowManager.LayoutParams.WRAP_CONTENT
                else -> (screenWidth * widthPercent).toInt()
            }
            height = when (heightPercent) {
                null -> WindowManager.LayoutParams.WRAP_CONTENT
                else -> (screenHeight * heightPercent).toInt()
            }
            x = when (initX) {
                null -> when (widthPercent) {
                    null -> 0
                    else -> (screenWidth * (1f - widthPercent) / 2f).toInt()
                }
                else -> (screenWidth * initX).toInt()
            }
            y = when (initY) {
                null -> when (heightPercent) {
                    null -> 0
                    else -> (screenHeight * (1f - heightPercent) / 2f).toInt()
                }
                else -> (screenHeight * initY).toInt()
            }
        }

        windowManager.addView(view, layoutParams)
    }
}