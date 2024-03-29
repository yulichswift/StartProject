package com.jeff.startproject.ui.db

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeff.startproject.databinding.ActivityDbBinding
import com.jeff.startproject.vo.db.DbResource
import com.jeff.startproject.vo.db.User
import com.log.JFLog
import com.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@AndroidEntryPoint
class DbActivity : BaseActivity<ActivityDbBinding>() {

    private val viewModel: DbViewModel by viewModels()

    override fun getViewBinding(): ActivityDbBinding {
        return ActivityDbBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                supervisorScope {
                    launch {
                        viewModel.dbSingleResource.collectLatest {
                            JFLog.d("$it")

                            when (it) {
                                is DbResource.Loading -> {
                                    binding.layoutQuery.error = ""
                                }
                                is DbResource.Loaded -> {
                                }
                                is DbResource.Failure -> {
                                    binding.layoutQuery.error = it.throwable.localizedMessage
                                }
                                is DbResource.SuccessNoContent -> {
                                    binding.layoutQuery.error = "Db result empty"
                                }
                                is DbResource.Success -> {
                                }
                            }
                        }
                    }

                    launch {
                        viewModel.dbListResource.collectLatest {
                            JFLog.d("$it")

                            when (it) {
                                is DbResource.Loading -> {
                                    binding.layoutQuery.error = ""
                                }
                                is DbResource.Loaded -> {
                                }
                                is DbResource.Failure -> {
                                    binding.layoutQuery.error = it.throwable.localizedMessage
                                }
                                is DbResource.SuccessNoContent -> {
                                    binding.layoutQuery.error = "Db result empty"
                                }
                                is DbResource.Success -> {
                                    JFLog.d("Query result:")
                                    it.data.forEach { user ->
                                        JFLog.d("$user")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.btnInsert.setOnClickListener {
            mutableListOf<User>().also {
                it.add(User(userId = "001", userName = "Jeff", age = 35))
                it.add(User(userId = "002", userName = "Android", age = 10))
                it.add(User(userId = "003", userName = "iOS", age = 10))
            }.also {
                viewModel.insertUsers(it)
            }
        }

        binding.btnQueryAll.setOnClickListener {
            viewModel.queryUserList()
        }

        binding.btnNukeTable.setOnClickListener {
            viewModel.nukeTable()
        }

        binding.btnQueryBy.setOnClickListener {
            binding.editQuery.text.toString().also {
                viewModel.queryUserByName(it)
            }
        }

        binding.btnQueryLike.setOnClickListener {
            binding.editQuery.text.toString().also {
                viewModel.queryUserLikeName(it)
            }
        }
    }
}
