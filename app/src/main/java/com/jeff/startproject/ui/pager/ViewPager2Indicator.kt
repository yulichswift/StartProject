package com.jeff.startproject.ui.pager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.jeff.startproject.R

class ViewPager2Indicator(context: Context, attrs: AttributeSet) : View(context, attrs) {
    init {
        setDefaultStyleable(context, attrs)
    }

    private lateinit var mUnselectedPaint: Paint
    private lateinit var mSelectedPaint: Paint
    private var mPosition = 0
    private var mCycleNumber = 0
    private var mRadius = 0f
    private var mLength = 0f
    private var mOffset = 0f
    private var mDistance = 0f
    private var mPercent = 0f
    private var mIsLeft = false
    private var mAnimation = false
    private val mRectClose = RectF()
    private val mRectOpen = RectF()

    private fun setDefaultStyleable(context: Context, attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPager2Indicator)
        val selectedColor = typedArray.getColor(R.styleable.ViewPager2Indicator_indicator_selected_color, Color.BLACK)
        val unselectedColor = typedArray.getColor(R.styleable.ViewPager2Indicator_indicator_unselected_color, selectedColor)
        mRadius = typedArray.getDimensionPixelSize(R.styleable.ViewPager2Indicator_indicator_radius, 0).toFloat()
        mDistance = typedArray.getDimensionPixelSize(R.styleable.ViewPager2Indicator_indicator_distance, 0).toFloat()
        typedArray.recycle()

        mLength = 2f * mRadius
        mCycleNumber = 0
        mAnimation = true

        mUnselectedPaint = Paint().also {
            it.style = Paint.Style.FILL
            it.color = unselectedColor
            it.isAntiAlias = true
            it.strokeWidth = 3f
        }

        mSelectedPaint = Paint().also {
            it.style = Paint.Style.FILL
            it.color = selectedColor
            it.isAntiAlias = true
            it.strokeWidth = 3f
        }

        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (mCycleNumber <= 0) {
            return
        }

        canvas.translate(width / 2f, height / 2f)

        if (mPosition == mCycleNumber - 1) {
            val leftClose = -mCycleNumber * .5f * mDistance - mRadius
            val rightClose = leftClose + 2f * mRadius + mOffset
            val topClose = -mRadius
            val bottomClose = mRadius
            mRectClose.set(leftClose, topClose, rightClose, bottomClose)
            val paint = if (mOffset == 0f) mUnselectedPaint else mSelectedPaint
            canvas.drawRoundRect(mRectClose, mRadius, mRadius, paint)

            for (i in 1 until mCycleNumber) {
                canvas.drawCircle(rightClose - mRadius + i * mDistance, 0f, mRadius, mUnselectedPaint)
            }

            val rightOpen = -mCycleNumber * .5f * mDistance + mCycleNumber * mDistance + mRadius
            val leftOpen = rightOpen - 2f * mRadius - mDistance + mOffset
            val topOpen = -mRadius
            val bottomOpen = mRadius
            mRectOpen.set(leftOpen, topOpen, rightOpen, bottomOpen)
            canvas.drawRoundRect(mRectOpen, mRadius, mRadius, mSelectedPaint)
        } else {
            for (i in 0..mCycleNumber) {
                // if (i in 0..mPosition || i > mPosition)
                canvas.drawCircle(-mCycleNumber * .5f * mDistance + i * mDistance, 0f, mRadius, mUnselectedPaint)
            }

            val leftClose = -mCycleNumber * .5f * mDistance + mPosition * mDistance - mRadius + mOffset
            val rightClose = leftClose + 2f * mRadius + mDistance
            val topClose = -mRadius
            val bottomClose = mRadius
            mRectClose.set(leftClose, topClose, rightClose, bottomClose)
            canvas.drawRoundRect(mRectClose, mRadius, mRadius, mSelectedPaint)
        }
    }

    private fun move(percent: Float, position: Int, isLeft: Boolean) {
        this.mPosition = position
        this.mPercent = percent
        this.mIsLeft = isLeft
        this.mOffset = percent * mDistance
        invalidate()
    }

    fun setCycleNumber(number: Int) = apply {
        mCycleNumber = number
        invalidate()
    }

    fun setRadius(radius: Float) = apply {
        this.mRadius = radius
        invalidate()
    }

    fun setDistance(distance: Float) = apply {
        this.mDistance = distance
        invalidate()
    }

    fun setColor(selected: Int, unselected: Int) = apply {
        mSelectedPaint = Paint().also {
            it.style = Paint.Style.FILL
            it.color = selected
            it.isAntiAlias = true
            it.strokeWidth = 3f
        }

        mUnselectedPaint = Paint().also {
            it.style = Paint.Style.FILL
            it.color = unselected
            it.isAntiAlias = true
            it.strokeWidth = 3f
        }
    }

    fun setViewPager(viewpager: ViewPager2, cycleNumber: Int) = apply {
        mCycleNumber = cycleNumber
        viewpager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            private var lastValue = -1
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (!mAnimation) {
                    return
                }
                var isLeft = mIsLeft
                if (lastValue / 10 > positionOffsetPixels / 10) {
                    //右滑
                    isLeft = false
                } else if (lastValue / 10 < positionOffsetPixels / 10) {
                    //左滑
                    isLeft = true
                }
                if (mCycleNumber > 0) {
                    move(positionOffset, position % mCycleNumber, isLeft)
                }
                lastValue = positionOffsetPixels
            }

            override fun onPageSelected(position: Int) {
                if (mAnimation) {
                    return
                }
                if (mCycleNumber > 0) {
                    move(0f, position % mCycleNumber, false)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }
}