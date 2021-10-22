package com.jeff.startproject.ui.base

import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.ui.base.BaseActivity

abstract class ProgressActivity<B : ViewBinding> : BaseActivity<B>() {

    val progressHUD: KProgressHUD by lazy {
        KProgressHUD.create(this).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(false)
    }

    override fun onDestroy() {
        super.onDestroy()

        progressHUD.dismiss()
    }
}