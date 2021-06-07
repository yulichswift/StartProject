package com.jeff.startproject.view.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.jeff.startproject.databinding.ActivityReceiveBinding
import com.view.base.BaseActivity

class ReceiveActivity : BaseActivity<ActivityReceiveBinding>() {
    override fun getViewBinding() = ActivityReceiveBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val req = intent.getStringExtra("req")
        binding.receivedTv.text = req
        binding.okBtn.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("answer", "$req ok")
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
        binding.cancelBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}