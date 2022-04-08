package com.jeff.startproject.ui.bottomsheet

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jeff.startproject.databinding.ActivityBottomSheetBinding
import com.ui.base.BaseActivity

class BottomSheetActivity : BaseActivity<ActivityBottomSheetBinding>() {
    override fun getViewBinding(): ActivityBottomSheetBinding {
        return ActivityBottomSheetBinding.inflate(layoutInflater)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bottomSheetBehavior = BottomSheetBehavior.from(binding.viewBottomSheet)

        binding.expandedBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.collapsedBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.hiddenBtn.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.peekHeightEdit.addTextChangedListener(
            onTextChanged = { s, start, before, count ->
                when (count) {
                    0 -> 0
                    else -> s?.toString()?.toIntOrNull() ?: 0
                }.also {
                    bottomSheetBehavior.setPeekHeight(it * 3, true)
                }
            }
        )

        binding.hideableBtn.setOnClickListener {
            bottomSheetBehavior.isHideable = bottomSheetBehavior.isHideable.not()

            when (bottomSheetBehavior.isHideable) {
                true -> "Is hideable"
                false -> "Is not hideable"
            }.also {
                binding.hideableBtn.text = it
            }

            binding.hiddenBtn.isEnabled = bottomSheetBehavior.isHideable
        }

        binding.draggableBtn.setOnClickListener {
            bottomSheetBehavior.isDraggable = bottomSheetBehavior.isDraggable.not()

            when (bottomSheetBehavior.isDraggable) {
                true -> "Is draggable"
                false -> "Is not draggable"
            }.also {
                binding.draggableBtn.text = it
            }
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
                    BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
                    BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
                    BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
                    BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
                    else -> "Not defined"
                }.also {
                    binding.onStateChangedTv.text = it
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.onSlideTv.text = "Slide offset: $slideOffset"
            }
        })
    }
}
