package com.jeff.startproject.ui.vector

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Toast
import com.jeff.startproject.R
import com.jeff.startproject.databinding.ActivityVectorBinding
import com.ui.base.BaseActivity
import com.utils.extension.repeatAnimation

/*
 * https://shapeshifter.design
 */
class VectorActivity : BaseActivity<ActivityVectorBinding>() {

    override fun getViewBinding(): ActivityVectorBinding {
        return ActivityVectorBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_share -> {
                    Toast.makeText(this, R.string.text_animation, Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        binding.toolbar.navigationIcon?.repeatAnimation(binding.toolbar)

        binding.ivSmile.repeatAnimation()
        binding.ivCar.repeatAnimation()

        var shareObjectAnimator: ObjectAnimator? = null
        binding.ivShare.setOnClickListener {
            shareObjectAnimator?.cancel()

            val startValue = binding.ivShare.rotation
            shareObjectAnimator = ObjectAnimator.ofFloat(binding.ivShare, "rotation", startValue, startValue + 45f).apply {
                duration = 500L
                start()
            }

            // Or use ValueAnimator
            // ValueAnimator.ofFloat(startPoint, endPoint)
        }

        binding.widthAniLayout.setOnClickListener {
            doWidthAni()
        }
    }

    override fun onResume() {
        super.onResume()

        doWidthAni()
    }

    private fun doWidthAni() {
        val paint = binding.textView.paint

        /*
        val bounds = Rect()
        paint.getTextBounds(binding.textView.text, 0, binding.textView.text.length, bounds)
        val textWidth = bounds.width()
        */

        val textWidth = paint.measureText(binding.textView.text.toString())

        val ani = ValueAnimator.ofInt(0, textWidth.toInt() + 5 * 2 * 3)
        ani.addUpdateListener {
            val lp = binding.widthAniLayout.layoutParams
            lp.width = it.animatedValue as Int
            binding.widthAniLayout.layoutParams = lp
        }
        ani.duration = 2000L
        ani.start()
    }
}