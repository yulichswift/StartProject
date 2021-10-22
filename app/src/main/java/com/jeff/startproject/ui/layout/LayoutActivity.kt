package com.jeff.startproject.ui.layout

import com.jeff.startproject.databinding.ActivityLayoutBinding
import com.ui.base.BaseActivity

class LayoutActivity : BaseActivity<ActivityLayoutBinding>() {
    override fun getViewBinding(): ActivityLayoutBinding {
        return ActivityLayoutBinding.inflate(layoutInflater)
    }
}