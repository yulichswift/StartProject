package com.jeff.startproject.view.text

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.jeff.startproject.databinding.ActivityTextBinding
import com.view.base.BaseActivity

class TextActivity : BaseActivity<ActivityTextBinding>() {
    override fun getViewBinding(): ActivityTextBinding {
        return ActivityTextBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val stringBuilder = SpannableStringBuilder("0123456789".let {
            val sb = StringBuilder()
            repeat(10) { _ ->
                sb.append(it)
            }
            sb.toString()
        })

        /*
        // 圖片
        run {
            var i = 0
            while (i >= 0) {
                i = stringBuilder.indexOf("5", i, false)
                if (i >= 0) {
                    stringBuilder.setSpan(ImageSpan(this, R.drawable.ic_smile), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    i++
                }
            }
        }
        */

        // Start: index
        // End: index不算
        // 3, 6: 實際3到5
        stringBuilder.setSpan(ForegroundColorSpan(Color.RED), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tv1.text = stringBuilder
    }
}