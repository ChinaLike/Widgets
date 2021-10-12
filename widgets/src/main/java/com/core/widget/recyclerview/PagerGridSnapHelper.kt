package com.core.widget.recyclerview

import android.view.View
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import kotlin.math.abs

/**
 * 分页居中
 * @author like
 * @date 10/12/21 2:19 PM
 */
class PagerGridSnapHelper : SnapHelper() {

    /**
     * Fling 阀值，滚动速度超过该阀值才会触发滚动
     */
    var flingThreshold: Int = 1000

    private var recyclerView: RecyclerView? = null

    override fun attachToRecyclerView(recyclerView: RecyclerView?) {
        super.attachToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }


    /**
     * Override this method to snap to a particular point within the target view or the container
     * view on any axis.
     *
     *
     * This method is called when the [SnapHelper] has intercepted a fling and it needs
     * to know the exact distance required to scroll by in order to snap to the target view.
     *
     * @param layoutManager the [RecyclerView.LayoutManager] associated with the attached
     * [RecyclerView]
     * @param targetView the target view that is chosen as the view to snap
     *
     * @return the output coordinates the put the result into. out[0] is the distance
     * on horizontal axis and out[1] is the distance on vertical axis.
     */
    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val pos = layoutManager.getPosition(targetView)
        var offset = IntArray(2)
        if (layoutManager is PagerGridLayoutManager) {
            offset = layoutManager.getSnapOffset(pos)
        }
        return offset
    }

    /**
     * Override this method to provide a particular target view for snapping.
     *
     *
     * This method is called when the [SnapHelper] is ready to start snapping and requires
     * a target view to snap to. It will be explicitly called when the scroll state becomes idle
     * after a scroll. It will also be called when the [SnapHelper] is preparing to snap
     * after a fling and requires a reference view from the current set of child views.
     *
     *
     * If this method returns `null`, SnapHelper will not snap to any view.
     *
     * @param layoutManager the [RecyclerView.LayoutManager] associated with the attached
     * [RecyclerView]
     *
     * @return the target view to which to snap on fling or end of scroll
     */
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        return (layoutManager as? PagerGridLayoutManager)?.findSnapView()
    }

    /**
     * Override to provide a particular adapter target position for snapping.
     *
     * @param layoutManager the [RecyclerView.LayoutManager] associated with the attached
     * [RecyclerView]
     * @param velocityX fling velocity on the horizontal axis
     * @param velocityY fling velocity on the vertical axis
     *
     * @return the target adapter position to you want to snap or [RecyclerView.NO_POSITION]
     * if no snapping should happen
     */
    override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager?, velocityX: Int, velocityY: Int): Int {
        var target = RecyclerView.NO_POSITION
        if (layoutManager is PagerGridLayoutManager) {
            if (layoutManager.canScrollHorizontally()) {
                if (velocityX > flingThreshold) {
                    target = layoutManager.findNextPageFirstPos()
                } else if (velocityX < -flingThreshold) {
                    target = layoutManager.findPrePageFirstPos()
                }
            } else if (layoutManager.canScrollVertically()) {
                if (velocityY > flingThreshold) {
                    target = layoutManager.findNextPageFirstPos()
                } else if (velocityY < -flingThreshold) {
                    target = layoutManager.findPrePageFirstPos()
                }
            }
        }
        return target
    }

    override fun onFling(velocityX: Int, velocityY: Int): Boolean {
        val layoutManager = recyclerView?.layoutManager ?: return false
        if (recyclerView?.adapter == null) {
            return false
        }
        return (abs(velocityY) > flingThreshold || abs(velocityX) > flingThreshold) && snapFromFling(layoutManager, velocityX, velocityY)
    }

    private fun snapFromFling(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Boolean {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider) {
            return false
        }
        val smoothScroller = snapScroller(layoutManager) ?: return false
        val targetPosition = findTargetSnapPosition(layoutManager, velocityX, velocityY)
        if (targetPosition == RecyclerView.NO_POSITION) {
            return false
        }
        smoothScroller.targetPosition = targetPosition
        layoutManager.startSmoothScroll(smoothScroller)
        return true
    }

    private fun snapScroller(layoutManager: RecyclerView.LayoutManager): LinearSmoothScroller? {
        if (layoutManager !is RecyclerView.SmoothScroller.ScrollVectorProvider || recyclerView == null) {
            return null
        }
        return PagerGridSmoothScroller(recyclerView!!)
    }
}