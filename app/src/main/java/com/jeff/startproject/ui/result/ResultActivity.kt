package com.jeff.startproject.ui.result

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.jeff.startproject.databinding.ActivityResultBinding
import com.ui.base.BaseActivity

class ResultActivity : BaseActivity<ActivityResultBinding>() {

    override fun getViewBinding() = ActivityResultBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.firstBtn.setOnClickListener {
            Intent(this, ReceiveActivity::class.java).also {
                it.putExtra("req", "First")
                firstResult.launch(it)
            }
        }
    }

    private val firstResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            binding.resultTv.text = intent?.getStringExtra("answer")
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            binding.resultTv.text = "First cancel"
        }
    }
}