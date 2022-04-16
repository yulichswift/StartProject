package com.jeff.startproject.ui.drag

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.view.View

class MyDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
    private val captureDrawable by lazy {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        // 將 view 的畫面繪製到畫布上
        view.draw(canvas)
        BitmapDrawable(view.resources, bitmap)
    }

    // Defines a callback that sends the drag shadow dimensions and touch point
    // back to the system.
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        // Set the width of the shadow to half the width of the original View.
        val width: Int = view.width

        // Set the height of the shadow to half the height of the original View.
        val height: Int = view.height

        // The drag shadow is a ColorDrawable. This sets its dimensions to be the
        // same as the Canvas that the system provides. As a result, the drag shadow
        // fills the Canvas.
        captureDrawable.setBounds(0, 0, width, height)

        // Set the size parameter's width and height values. These get back to
        // the system through the size parameter.
        size.set(width, height)

        // Set the touch point's position to be in the middle of the drag shadow.
        touch.set(width / 2, height / 2)
    }

    // Defines a callback that draws the drag shadow in a Canvas that the system
    // constructs from the dimensions passed to onProvideShadowMetrics().
    override fun onDrawShadow(canvas: Canvas) {
        // Draw the ColorDrawable on the Canvas passed in from the system.
        captureDrawable.draw(canvas)
    }
}
