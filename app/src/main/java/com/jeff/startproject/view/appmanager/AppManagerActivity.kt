package com.jeff.startproject.view.appmanager

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityAppManagerBinding
import com.log.JFLog
import com.utils.extension.throttleFirst
import com.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
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
            AppManagerViewModel.AppType.values().getOrNull(position)?.also {
                viewModel.loadList(packageManager, it)
            }
        }
        binding.autoImageText.setOnItemClickListener { _, _, position, _ ->
            AppManagerViewModel.IconType.values().getOrNull(position)?.also {
                adapter.iconType = it
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                launch {
                    viewModel.onListFlow()
                        .collectLatest {
                            binding.totalLabel.text = "Installed ${it.size}"
                            adapter.submitList(it)
                        }
                }

                launch {
                    filterFlow
                        .debounce(500L)
                        .collectLatest { viewModel.filterCurrentList(it) }
                }

                launch {
                    adapter.onRowSelected()
                        .throttleFirst(1_000L)
                        .collectLatest {
                            try {
                                val intent = packageManager.getLaunchIntentForPackage(it.packageName)
                                startActivity(intent)

                                viewModel.selectedApp(it.packageName)
                            } catch (e: Exception) {
                                JFLog.e(e)
                                Toast.makeText(this@AppManagerActivity, "Fail", Toast.LENGTH_LONG).show()
                            }
                        }
                }

                launch {
                    adapter.onClickedRemove()
                        .throttleFirst(1_000L)
                        .collectLatest {
                            when (it.appType) {
                                AppManagerViewModel.AppType.Recent -> viewModel.clearRecentApp(packageManager, it.packageName)
                                else -> {
                                    val intent = Intent().apply {
                                        action = Intent.ACTION_DELETE
                                        data = Uri.parse("package:${it.packageName}")
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    startActivity(intent)
                                }
                            }
                        }
                }
            }
        }

        binding.editFilter.doOnTextChanged { chars, _, _, _ ->
            filterFlow.tryEmit(chars)
        }

        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.loadList(packageManager, viewModel.currentAppType)
                delay(1_000L)
                binding.refreshLayout.isRefreshing = false
            }
        }

        lifecycleScope.launchWhenResumed {
            val initAppType = viewModel.currentAppType

            binding.autoAppText.setText(initAppType.name)
            binding.autoImageText.setText(adapter.iconType.name)
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, AppManagerViewModel.AppType.values().map { it.name }).also { binding.autoAppText.setAdapter(it) }
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, AppManagerViewModel.IconType.values().map { it.name }).also { binding.autoImageText.setAdapter(it) }

            viewModel.loadList(packageManager, initAppType)
        }
    }
}