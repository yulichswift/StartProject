package com.jeff.startproject.ui.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jeff.startproject.databinding.ActivityBottomSheet2Binding
import com.ui.base.BaseActivity

class BottomSheetActivity2 : BaseActivity<ActivityBottomSheet2Binding>() {
    override fun getViewBinding(): ActivityBottomSheet2Binding {
        return ActivityBottomSheet2Binding.inflate(layoutInflater)
    }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var currentState = BottomSheetBehavior.STATE_COLLAPSED

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

                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED, BottomSheetBehavior.STATE_HIDDEN -> View.GONE
                    BottomSheetBehavior.STATE_DRAGGING, BottomSheetBehavior.STATE_EXPANDED -> View.VISIBLE
                    BottomSheetBehavior.STATE_SETTLING -> null
                    else -> null
                }?.also {
                    binding.backgroundView.visibility = it
                    binding.dashboard.visibility = it
                }

                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_EXPANDED) {
                    currentState = newState
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                binding.onSlideTv.text = "Slide offset: $slideOffset"

                updateView(slideOffset)
            }
        })

        binding.backgroundView.setOnClickListener {
            binding.collapsedBtn.performClick()
        }

        binding.dashboard.setOnClickListener {
            // 避免觸發隱藏的畫面
        }
    }

    private fun updateView(slideOffset: Float) {
        if (currentState == BottomSheetBehavior.STATE_COLLAPSED) {
            (slideOffset * 2f).let {
                if (it > 1f) 1f
                else it
            }.also {
                binding.bar.alpha = 1f - it
                binding.backgroundView.alpha = it
                binding.dashboard.alpha = it
            }
        } else {
            (1f - slideOffset).also {
                binding.bar.alpha = it
                binding.backgroundView.alpha = 1f - it
                binding.dashboard.alpha = 1f - it
            }
        }
    }
}
