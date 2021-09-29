package com.jeff.startproject.view.appmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityAppManagerBinding
import com.utils.extension.throttleFirst
import com.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AppManagerActivity : BaseActivity<ActivityAppManagerBinding>() {

    override fun getViewBinding(): ActivityAppManagerBinding {
        return ActivityAppManagerBinding.inflate(layoutInflater)
    }

    private val viewModel: AppManagerViewModel by viewModels()
    private lateinit var adapter: AppAdapter
    private var canRemove = false

    private val filterFlow = MutableSharedFlow<CharSequence?>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = AppAdapter(packageManager)

        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter

        binding.autoAppText.setOnItemClickListener { _, _, position, _ ->
            AppManagerViewModel.AppType.values().getOrNull(position)?.also { viewModel.loadList(packageManager, it) }
        }
        binding.autoImageText.setOnItemClickListener { _, _, position, _ ->
            AppManagerViewModel.IconType.values().getOrNull(position)?.also {
                adapter.iconType = it
            }
        }

        lifecycleScope.launch {
            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                    viewModel.onListFlow()
                        .collectLatest {
                            binding.totalLabel.text = "Installed ${it.size}"
                            adapter.submitList(it)
                        }
                }
            }

            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    filterFlow
                        .debounce(500L)
                        .collectLatest { viewModel.filterCurrentList(it) }
                }
            }

            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    adapter.onRowSelected()
                        .throttleFirst(1_000L)
                        .collectLatest {
                            val intent = packageManager.getLaunchIntentForPackage(it)
                            startActivity(intent)
                        }
                }
            }

            launch {
                lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    adapter.onClickedRemove()
                        .throttleFirst(1_000L)
                        .collectLatest {
                            if (canRemove) {
                                val intent = Intent().apply {
                                    action = Intent.ACTION_DELETE
                                    data = Uri.parse("package:$it")
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }
                                startActivity(intent)
                            }
                        }
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            val initAppType = AppManagerViewModel.AppType.NonSystem
            binding.autoAppText.setText(initAppType.name)
            binding.autoImageText.setText(adapter.iconType.name)
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, AppManagerViewModel.AppType.values().map { it.name }).also { binding.autoAppText.setAdapter(it) }
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, AppManagerViewModel.IconType.values().map { it.name }).also { binding.autoImageText.setAdapter(it) }
            
            viewModel.loadList(packageManager, initAppType)
        }

        binding.editFilter.doOnTextChanged { chars, _, _, _ ->
            filterFlow.tryEmit(chars)
        }
    }
}