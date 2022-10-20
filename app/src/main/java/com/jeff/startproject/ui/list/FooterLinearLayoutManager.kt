package com.jeff.startproject.ui.list

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// https://stackoverflow.com/questions/41253811/make-last-item-viewholder-in-recyclerview-fill-rest-of-the-screen-or-have-a-mi

class FooterLinearLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun layoutDecoratedWithMargins(child: View, left: Int, top: Int, right: Int, bottom: Int) {
        val lp = child.layoutParams as RecyclerView.LayoutParams
        if (lp.absoluteAdapterPosition < itemCount - 1)
            return super.layoutDecoratedWithMargins(child, left, top, right, bottom)

        val parentBottom = height - paddingBottom
        return if (bottom < parentBottom) {
            val offset = parentBottom - bottom
            super.layoutDecoratedWithMargins(child, left, top + offset, right, bottom + offset)
        } else {
            super.layoutDecoratedWithMargins(child, left, top, right, bottom)
        }
    }
}