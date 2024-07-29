package com.steve_md.smartmkulima.utils

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.steve_md.smartmkulima.R


class DashboardIndicatorLayoutMerchant : LinearLayout {

    private var indicatorCount: Int = 0
    private var selectedPosition: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initIndicators(context, attrs, 0)
    }

    constructor(
        context: Context, attrs: AttributeSet, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        initIndicators(context, attrs, defStyleAttr)
    }

    private fun initIndicators(context: Context, attrs: AttributeSet, defStyleAttr: Int) {
//        val typedArray = context.obtainStyledAttributes(
//            attrs,
//            R.styleable.IndicatorLayout, defStyleAttr, 0
//        )

        try {
           // indicatorCount = typedArray.getInt(R.styleable.IndicatorLayout_indicatorCount, 0)
        } finally {
           // typedArray.recycle()
        }
        updateIndicators()

    }

    private fun px(dpValue: Float): Int {
        return (dpValue * context.resources.displayMetrics.density).toInt()
    }

    private fun updateIndicators() {
        removeAllViews()
        for (i in 0 until indicatorCount) {
            val indicator = View(context)

            val layoutParams = LayoutParams(px(6f), px(6f))
            layoutParams.setMargins(px(3f), px(3f), px(3f), px(3f))
            indicator.layoutParams = layoutParams
           // indicator.setBackgroundResource(R.drawable.indicator_unselected_merchant)

            addView(indicator)
        }
    }

    fun setIndicatorCount(count: Int) {
        indicatorCount = count
        updateIndicators()
    }

    fun selectCurrentPosition(position: Int) {
        if (position in 0..indicatorCount) {
            selectedPosition = position
            for (index in 0 until indicatorCount) {
                val indicator = getChildAt(index)
                if (index == selectedPosition) {
                   //  indicator.setBackgroundResource(R.drawable.dashboard_indicator_selected_merchant)
                } else {
                   // indicator.setBackgroundResource(R.drawable.dashboard_indicator_unselected_merchant)
                }
            }
        }
    }

}