package com.jeff.startproject.view.flowcontrol

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jeff.startproject.databinding.ActivityFlowControlBinding
import com.log.JFLog
import com.view.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FlowControlActivity : BaseActivity<ActivityFlowControlBinding>() {

    private val viewModel: FlowControlViewModel by viewModels()

    override fun getViewBinding(): ActivityFlowControlBinding {
        return ActivityFlowControlBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.recordLog.observe(this, Observer { message ->
            binding.textLog.text = message
        })

        binding.btnCancel.setOnClickListener {
            viewModel.tryCancelPreviousThenRun()
        }

        binding.btnCancelTask1.setOnClickListener {
            viewModel.cancelTask()
        }

        binding.btnJoin.setOnClickListener {
            viewModel.tryJoinPreviousOrRun()
        }

        binding.btnCancelTask2.setOnClickListener {
            viewModel.cancelTask()
        }
    }


    override fun onResume() {
        super.onResume()

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        lifecycleScope.launch {
            delay(1000L)
            val rect = Rect()
            binding.btnJoin.getGlobalVisibleRect(rect)
            JFLog.d("Rect: $rect")
        }
    }
}