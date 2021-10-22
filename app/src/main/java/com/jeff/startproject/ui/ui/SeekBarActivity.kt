package com.jeff.startproject.ui.ui

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.jeff.startproject.databinding.ActivitySeekbarBinding
import com.ui.base.BaseActivity

class SeekBarActivity : BaseActivity<ActivitySeekbarBinding>() {
    override fun getViewBinding(): ActivitySeekbarBinding {
        return ActivitySeekbarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.seekBar2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.seekBar2ThumbMask.visibility = if (progress == 0) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}