package com.jeff.startproject.view.ui

import com.jeff.startproject.databinding.ActivitySeekbarBinding
import com.view.base.BaseActivity

class SeekBarActivity : BaseActivity<ActivitySeekbarBinding>() {
    override fun getViewBinding(): ActivitySeekbarBinding {
        return ActivitySeekbarBinding.inflate(layoutInflater)
    }
}