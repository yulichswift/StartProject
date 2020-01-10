package com.jeff.startproject.view.main

import android.content.Intent
import android.os.Bundle
import com.jeff.startproject.R
import com.jeff.startproject.view.base.BaseActivity
import com.jeff.startproject.view.eventbus.EventBusActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

// https://developer.android.google.cn/kotlin/ktx
// https://developer.android.google.cn/kotlin/coroutines

class MainActivity : BaseActivity() {

    private val viewModel by viewModel<MainViewModel>()

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_event_bus.setOnClickListener {
            Intent(this, EventBusActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}
