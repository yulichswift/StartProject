package com.jeff.startproject.ui.draw

import android.os.Bundle
import com.google.android.material.slider.Slider
import com.jeff.startproject.databinding.ActivityDrawBinding
import com.ui.base.BaseActivity

class DrawActivity : BaseActivity<ActivityDrawBinding>() {
    override fun getViewBinding(): ActivityDrawBinding = ActivityDrawBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.indicatorSlider.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.indicatorView.selectedIndex = value.toInt() - 1
            }
        })

        binding.progressSlider.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.drawProgressBar.progress = value
            }
        })
    }
}