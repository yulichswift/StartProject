package com.jeff.startproject.view.others

import com.jeff.startproject.databinding.ActivityCustomBinding
import com.view.base.BaseActivity

class CustomActivity : BaseActivity<ActivityCustomBinding>() {

    override fun getViewBinding(): ActivityCustomBinding {
        return ActivityCustomBinding.inflate(layoutInflater)
    }
}