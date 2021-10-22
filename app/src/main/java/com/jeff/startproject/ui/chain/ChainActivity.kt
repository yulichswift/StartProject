package com.jeff.startproject.ui.chain

import com.jeff.startproject.databinding.ActivityChainBinding
import com.ui.base.BaseActivity

/*
 * https://www.jowanxu.top/2018/01/15/Android-ConstraintLayout-Chains
 */

class ChainActivity : BaseActivity<ActivityChainBinding>() {
    override fun getViewBinding(): ActivityChainBinding {
        return ActivityChainBinding.inflate(layoutInflater)
    }
}