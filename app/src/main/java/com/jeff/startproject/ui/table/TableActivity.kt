package com.jeff.startproject.ui.table

import android.graphics.Color
import android.os.Bundle
import com.jeff.startproject.databinding.ActivityTableBinding
import com.jeff.startproject.databinding.RowSimpleBinding
import com.ui.base.BaseActivity
import kotlin.random.Random

class TableActivity : BaseActivity<ActivityTableBinding>() {
    override fun getViewBinding() = ActivityTableBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var count = 0

        binding.addBtn.setOnClickListener {
            val rowBinding = RowSimpleBinding.inflate(layoutInflater, null, false)
            val randomColor = Color.argb(
                Random.nextInt(128, 256),
                Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)
            )
            rowBinding.imageView.setBackgroundColor(randomColor)
            rowBinding.textView.text = "Jeff R${++count}"
            binding.tableLayout.addView(rowBinding.root)
        }

        binding.row1Img.setOnClickListener {
            binding.row1Text.text = "${binding.row1Text.text} R1"
        }
        binding.row2Img.setOnClickListener {
            binding.row2Text.text = "${binding.row2Text.text} R2"
        }
    }
}
