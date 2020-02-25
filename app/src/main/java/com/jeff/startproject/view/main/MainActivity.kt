package com.jeff.startproject.view.main

import android.content.Intent
import android.os.Bundle
import com.jeff.startproject.databinding.ActivityMainBinding
import com.jeff.startproject.view.base.BaseActivity
import com.jeff.startproject.view.db.DbActivity
import com.jeff.startproject.view.edittext.EditTextActivity
import com.jeff.startproject.view.eventbus.EventBusActivity
import com.jeff.startproject.view.flowcontrol.FlowControlActivity
import com.jeff.startproject.view.table.TableActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/*
 * https://developer.android.google.cn/kotlin/ktx
 *
 * https://developer.android.google.cn/kotlin/coroutines
 *
 * https://source.android.com/setup/contribute/code-style#follow-field-naming-conventions
 *
 * https://material.io/develop/android/components/material-card-view
 *
 * https://material.io/develop/android/components/material-button/
 *
 * https://developer.android.com/topic/libraries/view-binding
 */

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private val viewModel by viewModel<MainViewModel>()

    override fun getViewBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnEventBus.setOnClickListener {
            Intent(this, EventBusActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnTable.setOnClickListener {
            Intent(this, TableActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnEdit.setOnClickListener {
            Intent(this, EditTextActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnRoom.setOnClickListener {
            Intent(this, DbActivity::class.java).also {
                startActivity(it)
            }
        }

        binding.btnFlow.setOnClickListener {
            Intent(this, FlowControlActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}
