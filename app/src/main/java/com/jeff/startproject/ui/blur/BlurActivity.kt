package com.jeff.startproject.ui.blur

import android.os.Bundle
import com.jeff.startproject.databinding.ActivityBlurBinding
import com.ui.base.BaseActivity
import com.utils.extension.repeatAnimation

class BlurActivity : BaseActivity<ActivityBlurBinding>() {
    override fun getViewBinding(): ActivityBlurBinding {
        return ActivityBlurBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivCar.repeatAnimation()
    }
}