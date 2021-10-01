package com.jeff.startproject.view.list

import android.graphics.Color
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.startproject.databinding.ActivityDragBinding
import com.view.base.BaseActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class DragActivity : BaseActivity<ActivityDragBinding>() {
    override fun getViewBinding() = ActivityDragBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = mutableListOf<String>()
        repeat(100) {
            list.add("No. $it")
        }

        val adapter = DragAdapter()
        adapter.setList(list)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        val itemDragDropCallback = ItemDragDropCallback()
        val touchHelper = ItemTouchHelper(itemDragDropCallback)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        adapter.setItemTouchHelper(touchHelper)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    itemDragDropCallback.onRowMoved().collectLatest {
                        adapter.swapItem(it.first, it.second)
                    }
                }

                launch {
                    itemDragDropCallback.onRowSelected().collectLatest {
                        it.get()?.itemView?.setBackgroundColor(Color.LTGRAY)
                    }
                }

                launch {
                    itemDragDropCallback.onRowClear().collectLatest {
                        it.get()?.itemView?.setBackgroundColor(Color.TRANSPARENT)
                    }
                }
            }
        }
    }
}