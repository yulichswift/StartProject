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
import com.jeff.startproject.view.appmanager.enums.AppType
import com.jeff.startproject.view.appmanager.enums.IconType
import com.jeff.startproject.vo.api.ApiResource
import com.log.JFLog
import com.utils.extension.throttleFirst
import com.view.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

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
            AppType.values().getOrNull(position)?.also {
                viewModel.loadList(it)
            }
        }
        binding.autoImageText.setOnItemClickListener { _, _, position, _ ->
            IconType.values().getOrNull(position)?.also {
                adapter.iconType = it
            }
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                supervisorScope {
                    launch {
                        viewModel.onListFlow()
                            .collect {
                                when (it) {
                                    ApiResource.Loading -> setRefreshing(true)
                                    is ApiResource.LoadingContent -> {
                                        setRefreshing(true)
                                        binding.totalLabel.text = "Loading..."
                                        adapter.submitList(it.data)
                                    }
                                    is ApiResource.Success -> {
                                        val data = it.data
                                        binding.totalLabel.text = "Installed ${data.size}"
                                        adapter.submitList(data)
                                    }
                                    ApiResource.SuccessEmpty -> TODO("setRefreshing(false)")
                                    is ApiResource.Failure -> TODO("setRefreshing(false)")
                                    is ApiResource.Loaded -> setRefreshing(false)
                                }
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
                                    AppType.Recent -> viewModel.clearRecentApp(it.packageName)
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
        }

        binding.editFilter.doOnTextChanged { chars, _, _, _ ->
            filterFlow.tryEmit(chars)
        }

        binding.refreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewModel.loadList(viewModel.currentAppType)
            }
        }

        lifecycleScope.launchWhenResumed {
            val initAppType = viewModel.currentAppType

            binding.autoAppText.setText(initAppType.name)
            binding.autoImageText.setText(adapter.iconType.name)
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, AppType.values().map { it.name }).also { binding.autoAppText.setAdapter(it) }
            ArrayAdapter(this@AppManagerActivity, R.layout.simple_dropdown_item, IconType.values().map { it.name }).also { binding.autoImageText.setAdapter(it) }

            viewModel.loadList(initAppType)
        }
    }

    private fun setRefreshing(isRefreshing: Boolean) {
        if (isRefreshing) {
            if (!binding.refreshLayout.isRefreshing) {
            }
        } else {
            binding.refreshLayout.isRefreshing = isRefreshing
        }
    }
}