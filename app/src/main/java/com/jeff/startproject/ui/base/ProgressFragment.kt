package com.jeff.startproject.ui.base

import androidx.viewbinding.ViewBinding
import com.kaopiz.kprogresshud.KProgressHUD
import com.ui.base.BaseFragment

abstract class ProgressFragment<out B : ViewBinding> : BaseFragment<B>() {

    val progressHUD: KProgressHUD by lazy {
        KProgressHUD.create(activity).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(false)
    }
}