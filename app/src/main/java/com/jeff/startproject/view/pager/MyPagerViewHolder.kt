package com.jeff.startproject.view.pager

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.jeff.startproject.databinding.ItemViewPagerBinding

class MyPagerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemViewPagerBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun bind(item: MyPagerItem) {
        item.color?.also {
            val color = Color.rgb(it.first, it.second, it.third)
            itemView.setBackgroundColor(color)
        }

        binding.tvCenter.text = "Center ${item.title}"
        binding.tvTopStart.text = "TopStart ${item.subtitle}"
        binding.tvTopEnd.text = "TopEnd ${item.subtitle}"
        binding.tvBottomStart.text = "BottomStart ${item.subtitle}"
        binding.tvBottomEnd.text = "BottomEnd ${item.subtitle}"
    }
}