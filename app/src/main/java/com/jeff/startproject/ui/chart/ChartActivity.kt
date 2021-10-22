package com.jeff.startproject.ui.chart

import android.graphics.Color
import android.os.Bundle
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.jeff.startproject.databinding.ActivityChartBinding
import com.ui.base.BaseActivity


class ChartActivity : BaseActivity<ActivityChartBinding>() {
    override fun getViewBinding() = ActivityChartBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.chart1.let {
            it.setUsePercentValues(true)
            it.setExtraOffsets(5f, 10f, 5f, 5f)

            it.dragDecelerationFrictionCoef = .95f

            it.isDrawHoleEnabled = true
            it.setHoleColor(Color.WHITE)

            it.setTransparentCircleColor(Color.WHITE)
            it.setTransparentCircleAlpha(110)

            it.holeRadius = 58f
            it.transparentCircleRadius = 61f

            it.centerText = "MPAndroidChart"
            it.setDrawCenterText(true)

            it.rotationAngle = 0f

            it.isRotationEnabled = true
            it.isHighlightPerTapEnabled = true

            it.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onValueSelected(e: Entry?, h: Highlight?) {
                }

                override fun onNothingSelected() {
                }
            })

            it.setEntryLabelColor(Color.WHITE)
            // entry label styling
            it.setEntryLabelColor(Color.WHITE)
            it.setEntryLabelTextSize(12f)
            //chart.setEntryLabelTypeface(tfRegular)

            // SetData
            it.data = getData(5, 10f)
            // undo all highlights
            it.highlightValues(null)
            it.invalidate()
        }

        binding.chart1.legend.also {
            it.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            it.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            it.orientation = Legend.LegendOrientation.VERTICAL
            it.setDrawInside(false)
            it.xEntrySpace = 0f
            it.yEntrySpace = 0f
            it.yOffset = 0f
        }

        binding.chart1.description.also {
            it.isEnabled = true
            it.text = "Jeff"
        }
    }

    private fun getData(count: Int, range: Float): PieData {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    "Data $i",
                    null
                )
            )
        }
        val dataSet = PieDataSet(entries, "Random results")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors = ArrayList<Int>()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        //data.setValueTypeface(tfLight)

        return data
    }
}