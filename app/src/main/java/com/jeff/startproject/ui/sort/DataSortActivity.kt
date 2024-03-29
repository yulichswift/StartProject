package com.jeff.startproject.ui.sort

import android.os.Bundle
import com.jeff.startproject.databinding.ActivityDataSortBinding
import com.ui.base.BaseActivity
import kotlin.random.Random

// https://www.jianshu.com/p/a6aae0037f79

class DataSortActivity : BaseActivity<ActivityDataSortBinding>() {
    override fun getViewBinding(): ActivityDataSortBinding {
        return ActivityDataSortBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = arrayListOf<String>()

        repeat(1000) {
            list.add("Random: ${Random.nextInt()}")
        }

        // 產生排序List
        val other = list.sortedByDescending { it }

        // 對原本List排序
        list.sortByDescending { it }

        val sb = StringBuilder()
        for (string in list) {
            sb.append("$string\n")
        }

        binding.tv.text = sb.toString()
    }
}