package com.jeff.startproject.ui.draw

import com.jeff.startproject.databinding.ActivityDrawBinding
import com.ui.base.BaseActivity

class DrawActivity : BaseActivity<ActivityDrawBinding>() {
    override fun getViewBinding(): ActivityDrawBinding = ActivityDrawBinding.inflate(layoutInflater)
}