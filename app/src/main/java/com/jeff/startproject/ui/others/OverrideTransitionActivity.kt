package com.jeff.startproject.ui.others

import android.content.Intent
import android.os.Bundle
import com.jeff.startproject.databinding.ActivityOverrideTransitionBinding
import com.ui.base.BaseActivity

class OverrideTransitionActivity : BaseActivity<ActivityOverrideTransitionBinding>() {

    companion object {
        var pageCount = 0
    }

    private val currentPage = pageCount

    override fun getViewBinding() = ActivityOverrideTransitionBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tv1.text = when (currentPage) {
            1 -> "None"
            2 -> "Fade"
            else -> "Normal"
        }

        binding.btnNext.setOnClickListener {
            pageCount++

            startActivity(Intent(this, OverrideTransitionActivity::class.java))
            when (currentPage % 3) {
                1 -> overridePendingTransition(0, 0)
                2 -> overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }

    override fun onBackPressed() {
        finish()
        when (currentPage % 3) {
            1 -> overridePendingTransition(0, 0)
            2 -> overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}