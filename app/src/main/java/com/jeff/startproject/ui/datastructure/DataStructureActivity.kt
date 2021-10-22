package com.jeff.startproject.ui.datastructure

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.gson.internal.LinkedTreeMap
import com.jeff.startproject.databinding.ActivityDataStructureBinding
import com.log.JFLog
import com.ui.base.BaseActivity
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.LinkedHashMap

class DataStructureActivity : BaseActivity<ActivityDataStructureBinding>() {

    override fun getViewBinding(): ActivityDataStructureBinding {
        return ActivityDataStructureBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            binding.mapBtn.setOnClickListener {
                compareDifferentMap()
            }

            binding.findBtn.setOnClickListener {
                compareFind()
            }

            binding.foreachBtn.setOnClickListener {
                compareForeach()
            }
        }
    }

    private fun compareDifferentMap() {
        fun initMap(map: MutableMap<String, String>) {
            map["5"] = "A"
            map["2"] = "B"
            map["3"] = "C"
            map["1"] = "D"
            map["4"] = "E"
        }

        val hashMap = HashMap<String, String>().also(::initMap)
        JFLog.d("$hashMap")

        // 依據插入順序
        val linkedHashMap = LinkedHashMap<String, String>().also(::initMap)
        JFLog.d("$linkedHashMap")

        val linkedTreeMap = LinkedTreeMap<String, String>().also(::initMap)
        JFLog.d("$linkedTreeMap")

        // 依據Key排序
        val treeMap = TreeMap<String, String>().also(::initMap)
        JFLog.d("$treeMap")
    }

    private fun compareFind() {
        val list = mutableListOf<Int>()
        repeat(10000) { list.add(it) }

        val t1 = calculationTime {
            list.asSequence().map { it.toString() }.first { it == "555" }
        }
        val t2 = calculationTime {
            list.map { it.toString() }.first { it == "555" }
        }

        JFLog.d("Sequence: $t1")
        JFLog.d("List: $t2")
    }

    private fun compareForeach() {
        val list = mutableListOf<Int>()
        repeat(3) { list.add(it) }

        val t1 = calculationTime {
            list.asSequence().map {
                JFLog.d("A1: $it")
                it.toString()
            }.map {
                JFLog.d("B1: $it")
                "A $it"
            }.toList()
        }

        val t2 = calculationTime {
            list.map {
                JFLog.d("A2: $it")
                it.toString()
            }.map {
                JFLog.d("B2: $it")
                "A $it"
            }
        }

        JFLog.d("Sequence: $t1")
        JFLog.d("List: $t2")
    }


    private fun calculationTime(block: () -> Unit): Long {
        val time = System.currentTimeMillis()
        block.invoke()
        return System.currentTimeMillis() - time
    }
}