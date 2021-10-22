package com.jeff.startproject.ui.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.jeff.startproject.databinding.ActivitySampleBinding
import com.jeff.startproject.enums.ModelResource
import com.log.JFLog
import com.ui.base.BaseActivity

class SampleActivity : BaseActivity<ActivitySampleBinding>() {

    private val viewModel: SampleViewModel by viewModels()

    override fun getViewBinding(): ActivitySampleBinding {
        return ActivitySampleBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.recordLog.observe(this, Observer { message ->
            binding.textLog.text = message
        })

        ModelResource.Success("Happy").also {
            handleStringResult(it)
            transformResult(it)
        }

        ModelResource.Success(999).also {
            handleIntResult(it)
            transformResult(it)
        }

        ModelResource.Failure(Exception("Help!")).also {
            handleStringResult(it)
            handleIntResult(it)
            transformResult(it)
        }

        ModelResource.Failure(RuntimeException("Crash!")).also {
            handleStringResult(it)
            handleIntResult(it)
            transformResult(it)
        }

        ModelResource.Loading.also {
            handleStringResult(it)
            handleIntResult(it)
            transformResult(it)
        }
    }

    private fun handleStringResult(modelResource: ModelResource<String>) {
        when (modelResource) {
            is ModelResource.Success -> {
                JFLog.d("Data: ${modelResource.data}").also {
                    viewModel.appendMessage(it)
                }
            }
            is ModelResource.Failure -> {
                JFLog.e("Failure: ${modelResource.throwable.message}").also {
                    viewModel.appendMessage(it)
                }
            }
            ModelResource.Loading -> {
                JFLog.d("Loading").also {
                    viewModel.appendMessage(it)
                }
            }
        }
    }

    private fun handleIntResult(modelResource: ModelResource<Int>) {
        when (modelResource) {
            is ModelResource.Success -> {
                JFLog.d("Data: ${modelResource.data}").also {
                    viewModel.appendMessage(it)
                }
            }
            is ModelResource.Failure -> {
                JFLog.e("Failure: ${modelResource.throwable.message}").also {
                    viewModel.appendMessage(it)
                }
            }
            ModelResource.Loading -> {
                JFLog.d("Loading").also {
                    viewModel.appendMessage(it)
                }
            }
        }
    }

    private fun transformResult(modelResource: ModelResource<Any>) {
        modelResource.transform.also {
            JFLog.d(it).also {
                viewModel.appendMessage(it)
            }
        }
    }

    private val <T> T.everything: T
        get() = this

    private val <T> T.transform: String
        get() = when (this) {
            is ModelResource.Success<*> -> "Success"
            ModelResource.SuccessNoContent -> "SuccessNoContent"
            is ModelResource.Failure -> "Failure"
            ModelResource.Loading -> "Loading"
            ModelResource.Loaded -> "Loaded"
            else -> "?"
        }
}