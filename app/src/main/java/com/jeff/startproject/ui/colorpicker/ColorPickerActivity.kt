package com.jeff.startproject.ui.colorpicker

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import com.jeff.startproject.databinding.ActivityColorPickerBinding
import com.log.JFLog
import com.ui.base.BaseActivity
import okhttp3.internal.toHexString

class ColorPickerActivity : BaseActivity<ActivityColorPickerBinding>() {

    override fun getViewBinding() = ActivityColorPickerBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // -1: White
        // 0: Black

        var aValue = "ff"
        var rValue = "ff"
        var gValue = "ff"
        var bValue = "ff"

        val seekBarListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    percentOf255toHex(progress).let {
                        when (seekBar) {
                            binding.seekbarA -> aValue = it
                            binding.seekbarR -> rValue = it
                            binding.seekbarG -> gValue = it
                            binding.seekbarB -> bValue = it
                        }
                    }

                    val hex = "#$aValue$rValue$gValue$bValue"
                    try {
                        val color = Color.parseColor(hex)
                        binding.view1.setBackgroundColor(color)
                        binding.view2.setBackgroundColor(color)
                        "Color: $color ($hex)".also {
                            binding.label1.text = it
                            binding.label2.text = it
                        }
                    } catch (e: Exception) {
                        JFLog.e("Hex: $hex")
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        }

        fun initSeekBar(seekBar: SeekBar) {
            seekBar.progress = 100
            seekBar.setOnSeekBarChangeListener(seekBarListener)
        }

        initSeekBar(binding.seekbarA)
        initSeekBar(binding.seekbarR)
        initSeekBar(binding.seekbarG)
        initSeekBar(binding.seekbarB)
    }

    private fun percentOf255toHex(percent: Int): String {
        return (percent * 255 / 100).toHexString().let {
            if (it.count() == 1) "0$it" else it
        }
    }
}