package com.jeff.startproject.view.main

import com.jeff.startproject.R
import com.jeff.startproject.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

// https://developer.android.google.cn/kotlin/ktx
// https://developer.android.google.cn/kotlin/coroutines

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }
}
