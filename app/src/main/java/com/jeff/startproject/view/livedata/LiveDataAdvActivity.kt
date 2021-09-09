package com.jeff.startproject.view.livedata

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.jeff.startproject.databinding.ActivityLiveDataAdvBinding
import com.log.JFLog
import com.view.base.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LiveDataAdvActivity : BaseActivity<ActivityLiveDataAdvBinding>() {

    private val stopLiveData = MutableLiveData<String>()

    override fun getViewBinding(): ActivityLiveDataAdvBinding {
        return ActivityLiveDataAdvBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 當Activity在背景時, 不會觸發.
        stopLiveData.observe(this) {
            JFLog.d("Live data observe: $it")
            binding.btn.text = it
        }

        binding.btn.setOnClickListener {
            lifecycleScope.launch {
                delay(3000L)
                val string = System.currentTimeMillis().toString()
                stopLiveData.value = string
                JFLog.d("Live data set: $string")
            }
        }
    }

    override fun onStart() {
        JFLog.d("onStart")
        super.onStart()
    }

    override fun onResume() {
        JFLog.d("onResume")
        super.onResume()
    }
}