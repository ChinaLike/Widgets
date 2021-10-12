package com.core.widget.recyclerview

import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * 处理平滑滚动
 * @author like
 * @date 10/12/21 2:02 PM
 */
class PagerGridSmoothScroller(private val recyclerView: RecyclerView) : LinearSmoothScroller(recyclerView.context) {

    override fun onTargetFound(targetView: View, state: RecyclerView.State, action: Action) {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is PagerGridLayoutManager) {
            val pos = recyclerView.getChildAdapterPosition(targetView)
            val snapDistances = layoutManager.getSnapOffset(pos)
            val dx = snapDistances[0]
            val dy = snapDistances[1]
            val time = calculateTimeForScrolling(abs(dx).coerceAtLeast(abs(dy)))
            if (time > 0) {
                action.update(dx, dy, time, mDecelerateInterpolator)
            }
        }
    }

    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is PagerGridLayoutManager && displayMetrics != null) {
            return layoutManager.millisecondsPreInch / displayMetrics.densityDpi
        }
        return super.calculateSpeedPerPixel(displayMetrics)
    }

}