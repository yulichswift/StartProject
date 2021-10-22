package com.jeff.startproject.ui.preferences

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.jeff.startproject.databinding.ActivityPreferencesBinding
import com.ui.base.BaseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class PreferencesActivity : BaseActivity<ActivityPreferencesBinding>() {

    override fun getViewBinding(): ActivityPreferencesBinding {
        return ActivityPreferencesBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launchWhenResumed {
            MyDateStore.profileFlow
                .flowOn(Dispatchers.Main)
                .collect {
                    binding.edit.setText(it)
                }
        }

        binding.btnSend.setOnClickListener {
            lifecycleScope.launch {
                MyDateStore.updateContent(binding.edit.text.toString())
            }
        }
    }
}