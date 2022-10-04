package com.jeff.startproject.ui.clip

import android.os.Bundle
import com.google.android.material.slider.Slider
import com.jeff.startproject.databinding.ActivityClipBinding
import com.ui.base.BaseActivity

class ClipActivity : BaseActivity<ActivityClipBinding>() {
    override fun getViewBinding(): ActivityClipBinding = ActivityClipBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updatedClipLabel()

        binding.clipBth.setOnClickListener {
            binding.clipConstraintLayout.nextState()
            updatedClipLabel()
        }

        binding.slider.addOnChangeListener(Slider.OnChangeListener { _, value, fromUser ->
            if (fromUser) {
                binding.clipConstraintLayout.angleOffset = value
            }
        })
    }

    private fun updatedClipLabel() {
        binding.clipLabel.text = binding.clipConstraintLayout.state.toString()
    }
}
