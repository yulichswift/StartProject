package com.jeff.startproject.ui.file

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jeff.startproject.MyApplication
import com.jeff.startproject.R
import com.jeff.startproject.databinding.FragmentFileContentBinding
import com.jeff.startproject.enums.ModelResource
import com.jeff.startproject.ui.base.ProgressFragment
import com.jeff.startproject.ui.diaglog.ConfirmDialogFragment
import com.log.JFLog
import com.ui.base.NavigateItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

@AndroidEntryPoint
class FileContentFragment : ProgressFragment<FragmentFileContentBinding>() {

    val viewModel: FileContentViewModel by viewModels()

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentFileContentBinding =
        FragmentFileContentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewStatus.layoutParams.height = MyApplication.getStatusBarHeight()

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {
            // Handle the back button event
            ConfirmDialogFragment(getString(R.string.message_back_disable), false).show(parentFragmentManager, "Confirm")
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                supervisorScope {
                    launch {
                        viewModel.status.collectLatest { result ->
                            when (result) {
                                ModelResource.Loading -> progressHUD.show()
                                ModelResource.Loaded -> progressHUD.dismiss()
                                else -> Unit
                            }

                            when (result) {
                                is ModelResource.Success -> {
                                    result.data
                                }
                                is ModelResource.Failure -> {
                                    result.throwable.localizedMessage
                                }
                                else -> null
                            }?.also {
                                binding.tvContent.text = it
                            }

                            when (result) {
                                is ModelResource.Success -> R.color.silver
                                is ModelResource.Failure -> R.color.orange_red
                                ModelResource.Loading -> R.color.black
                                else -> null
                            }?.also {
                                binding.viewStatus.setBackgroundColor(resources.getColor(it, null))
                            }
                        }
                    }
                }
            }
        }

        arguments?.getString("path")?.also {
            viewModel.readFile(it, true)
        }

        binding.toolbar.setNavigationOnClickListener {
            navigateTo(NavigateItem.Up)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Attach activity
        JFLog.d("onAttach: $context")
    }

    override fun onDetach() {
        super.onDetach()

        JFLog.d("onDetach")
    }
}