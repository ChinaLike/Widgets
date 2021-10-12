package com.core.widget.recyclerview

import android.graphics.PointF
import android.graphics.Rect
import android.util.SparseArray
import android.view.View
import android.view.View.MeasureSpec.EXACTLY
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs


/**
 * 网格分页显示
 * @author like
 * @date 10/12/21 9:41 AM
 */
class PagerGridLayoutManager : RecyclerView.LayoutManager, RecyclerView.SmoothScroller.ScrollVectorProvider {

    companion object {
        /**
         * 垂直滚动
         */
        const val VERTICAL: Int = LinearLayoutManager.VERTICAL

        /**
         * 水平滚动
         */
        const val HORIZONTAL: Int = LinearLayoutManager.HORIZONTAL
    }

    /**
     * 滚动方向，默认水平滚动
     */
    @OrientationType
    private var orientation: Int = HORIZONTAL

    /**
     * 行数
     */
    private var rows: Int = 0

    /**
     * 列数
     */
    private var columns: Int = 0

    /**
     * 一页的数量
     */
    private var onePageSize: Int = 0

    /**
     * 上次页面总数
     */
    private var lastPageCount: Int = -1

    /**
     * 上次页面下标
     */
    private var lastPageIndex: Int = -1

    /**
     * 水平滚动距离（偏移量）
     */
    private var offsetX: Int = 0

    /**
     * 垂直滚动距离（偏移量）
     */
    private var offsetY: Int = 0

    /**
     * 最大允许滑动的宽度
     */
    private var maxScrollX: Int = 0

    /**
     * 最大允许滑动的高度
     */
    private var maxScrollY: Int = 0

    /**
     * 滚动状态
     */
    private var mScrollState: Int = RecyclerView.SCROLL_STATE_IDLE

    /**
     * 条目宽度
     */
    private var itemWidth: Int = 0

    /**
     * 条目高度
     */
    private var itemHeight: Int = 0

    /**
     * 已经使用空间，用于测量View
     */
    private var widthUsed: Int = 0

    /**
     * 已经使用空间，用于测量View
     */
    private var heightUsed: Int = 0

    /**
     * 是否允许连续滚动,true 允许，false 不允许
     */
    var allowContinuousScroll: Boolean = true

    /**
     * 是否在滚动过程中对页面变化回调,true：更新、false：不更新
     */
    var changeSelectInScrolling: Boolean = true

    /**
     * item显示区域
     */
    private val itemFrames: SparseArray<Rect> = SparseArray()

    private var recyclerView: RecyclerView? = null

    /**
     * 界面变化监听
     */
    var pageChangedListener: PageChangedListener? = null

    /**
     * 每一个英寸滚动需要的微秒数，数值越大，速度越慢
     */
    var millisecondsPreInch: Float = 60F

    @JvmOverloads
    constructor(rows: Int, columns: Int, @OrientationType orientation: Int = HORIZONTAL) {
        this.orientation = orientation
        this.rows = rows
        this.columns = columns
        onePageSize = rows * columns
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        super.onAttachedToWindow(view)
        this.recyclerView = view
    }

    /**
     * 处理布局的子View
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        //如果是preLayout则不重新布局
        if (state.isPreLayout || !state.didStructureChange()) {
            return
        }
        if (itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            //页面变化
            setPageCount(0)
            setPageIndex(0, false)
        } else {
            setPageCount(getTotalPageCount())
            setPageIndex(getPageIndexByOffset(), false)
        }
        var pageCount = itemCount / onePageSize
        if (itemCount % onePageSize != 0) {
            pageCount++
        }
        //计算可以滚动的最大数值，并对滚动距离进行修正
        if (canScrollHorizontally()) {
            maxScrollX = (pageCount - 1) * getUsableWidth()
            maxScrollY = 0
            if (offsetX > maxScrollX) {
                offsetX = maxScrollX
            }
        } else {
            maxScrollY = (pageCount - 1) * getUsableHeight()
            maxScrollX = 0
            if (offsetY > maxScrollY) {
                offsetY = maxScrollY
            }
        }

        if (itemWidth <= 0) {
            itemWidth = getUsableWidth() / columns
        }
        if (itemHeight <= 0) {
            itemHeight = getUsableHeight() / rows
        }

        widthUsed = getUsableWidth() - itemWidth
        heightUsed = getUsableHeight() - itemHeight

        //预存储两页的View显示区域
        for (i in 0 until onePageSize * 2) {
            getItemFrameByPosition(i)
        }

        if (offsetX == 0 && offsetY == 0) {
            for (i in 0 until onePageSize) {
                if (i >= itemCount) {
                    break
                }
                val view: View = recycler.getViewForPosition(i)
                addView(view)
                measureChildWithMargins(view, widthUsed, heightUsed)
            }
        }
        //回收和填充布局
        recycleAndFillItems(recycler, state, true)
    }

    override fun onLayoutCompleted(state: RecyclerView.State) {
        super.onLayoutCompleted(state)
        if (state.isPreLayout) {
            return
        }
        setPageCount(getTotalPageCount())
        setPageIndex(getPageIndexByOffset(), false)
    }

    /**
     * 水平滚动
     *
     * @param dx       滚动距离
     * @param recycler 回收器
     * @param state    滚动状态
     * @return 实际滚动距离
     */
    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val newX = offsetX + dx
        var result = dx
        if (newX > maxScrollX) {
            result = maxScrollX - offsetX
        } else if (newX < 0) {
            result = 0 - offsetX
        }
        offsetX += result
        setPageIndex(getPageIndexByOffset(), true)
        offsetChildrenHorizontal(-result)
        if (result > 0) {
            recycleAndFillItems(recycler, state, true)
        } else {
            recycleAndFillItems(recycler, state, false)
        }
        return result
    }

    /**
     * 垂直滚动
     *
     * @param dy       滚动距离
     * @param recycler 回收器
     * @param state    滚动状态
     * @return 实际滚动距离
     */
    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        val newY: Int = offsetY + dy
        var result = dy
        if (newY > maxScrollY) {
            result = maxScrollY - offsetY
        } else if (newY < 0) {
            result = 0 - offsetY
        }
        offsetY += result
        setPageIndex(getPageIndexByOffset(), true)
        offsetChildrenVertical(-result)
        if (result > 0) {
            recycleAndFillItems(recycler!!, state!!, true)
        } else {
            recycleAndFillItems(recycler!!, state!!, false)
        }
        return result
    }

    /**
     * 监听滚动状态，滚动结束后通知当前选中的页面
     *
     * @param state 滚动状态
     */
    override fun onScrollStateChanged(state: Int) {
        mScrollState = state
        super.onScrollStateChanged(state)
        if (state == RecyclerView.SCROLL_STATE_IDLE) {
            setPageIndex(getPageIndexByOffset(), false)
        }
    }


    /**
     * Create a default `LayoutParams` object for a child of the RecyclerView.
     *
     *
     * LayoutManagers will often want to use a custom `LayoutParams` type
     * to store extra information specific to the layout. Client code should subclass
     * [RecyclerView.LayoutParams] for this purpose.
     *
     *
     * *Important:* if you use your own custom `LayoutParams` type
     * you must also override
     * [.checkLayoutParams],
     * [.generateLayoutParams] and
     * [.generateLayoutParams].
     *
     * @return A new LayoutParams for a child view
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    /**
     * 处理测量逻辑
     *
     * @param [recycler]          RecyclerView
     * @param [state]             状态
     * @param [widthSpec]  宽度属性
     * @param [heightSpec] 高度属性
     */
    override fun onMeasure(recycler: RecyclerView.Recycler, state: RecyclerView.State, widthSpec: Int, heightSpec: Int) {
        super.onMeasure(recycler, state, widthSpec, heightSpec)
        val widthsize = View.MeasureSpec.getSize(widthSpec) //取出宽度的确切数值
        var widthmode = View.MeasureSpec.getMode(widthSpec) //取出宽度的测量模式

        val heightsize = View.MeasureSpec.getSize(heightSpec) //取出高度的确切数值
        var heightmode = View.MeasureSpec.getMode(heightSpec) //取出高度的测量模式

        // 将 wrap_content 转换为 match_parent
        if (widthmode != EXACTLY && widthsize > 0) {
            widthmode = EXACTLY
        }
        if (heightmode != EXACTLY && heightsize > 0) {
            heightmode = EXACTLY
        }
        setMeasuredDimension(
            View.MeasureSpec.makeMeasureSpec(widthsize, widthmode),
            View.MeasureSpec.makeMeasureSpec(heightsize, heightmode)
        )
    }

    /**
     * 是否可以水平滚动
     *
     * @return true 是，false 不是。
     */
    override fun canScrollHorizontally(): Boolean {
        return orientation == HORIZONTAL
    }

    /**
     * 是否可以垂直滚动
     *
     * @return true 是，false 不是。
     */
    override fun canScrollVertically(): Boolean {
        return orientation == VERTICAL
    }

    /**
     * Should calculate the vector that points to the direction where the target position
     * can be found.
     *
     *
     * This method is used by the [LinearSmoothScroller] to initiate a scroll towards
     * the target position.
     *
     *
     * The magnitude of the vector is not important. It is always normalized before being
     * used by the [LinearSmoothScroller].
     *
     *
     * LayoutManager should not check whether the position exists in the adapter or not.
     *
     * @param targetPosition the target position to which the returned vector should point
     * @return the scroll vector for a given position.
     */
    override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
        val pointF: PointF = PointF()
        val pos = getSnapOffset(targetPosition)
        pointF.x = pos[0].toFloat()
        pointF.y = pos[1].toFloat()
        return pointF
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView, state: RecyclerView.State, position: Int) {
        val targetPageIndex = getPageIndexByPos(position)
        smoothScrollToPage(targetPageIndex)
    }

    override fun scrollToPosition(position: Int) {
        val pageIndex = getPageIndexByPos(position)
        scrollToPage(pageIndex)
    }

    /**
     * 获取偏移量(为PagerGridSnapHelper准备)
     * 用于分页滚动，确定需要滚动的距离。
     * {@link PagerGridSnapHelper}
     *
     * @param [targetPosition] 条目下标
     */
    fun getSnapOffset(targetPosition: Int): IntArray {
        val offset = IntArray(2)
        val pos: IntArray = getPageLeftTopByPosition(targetPosition)
        offset[0] = pos[0] - offsetX
        offset[1] = pos[1] - offsetY
        return offset
    }

    /**
     * 找到下一页第一个条目的位置
     */
    fun findNextPageFirstPos(): Int {
        var page = lastPageIndex
        page++
        if (page >= getTotalPageCount()) {
            page = getTotalPageCount() - 1
        }
        return page * onePageSize
    }

    /**
     * 找到上一页的第一个条目的位置
     */
    fun findPrePageFirstPos(): Int {
        // 在获取时由于前一页的View预加载出来了，所以获取到的直接就是前一页
        var page = lastPageIndex
        page--
        if (page < 0) {
            page = 0
        }
        return page * onePageSize
    }

    /**
     * 根据条目下标获取该条目所在页面的左上角位置
     *
     * @param pos 条目下标
     * @return 左上角位置
     */
    private fun getPageLeftTopByPosition(pos: Int): IntArray {
        val leftTop = IntArray(2)
        val page: Int = getPageIndexByPos(pos)
        if (canScrollHorizontally()) {
            leftTop[0] = page * getUsableWidth()
            leftTop[1] = 0
        } else {
            leftTop[0] = 0
            leftTop[1] = page * getUsableHeight()
        }
        return leftTop
    }

    /**
     * 根据pos，获取该View所在的页面
     *
     * @param pos position
     * @return 页面的页码
     */
    private fun getPageIndexByPos(pos: Int): Int {
        return pos / onePageSize
    }

    /**
     * 设置页面总数
     * @param [count] 页面总数
     */
    private fun setPageCount(count: Int) {
        if (count >= 0) {
            if (pageChangedListener != null && count != lastPageCount) {
                pageChangedListener?.onPageSizeChanged(count)
            }
            lastPageCount = count
        }
    }

    /**
     * 设置当前选中页面
     * @param [selectIndex] 当前选中页面
     * @param [isScrolling] 是否处于滚动状态
     */
    private fun setPageIndex(selectIndex: Int, isScrolling: Boolean) {
        if (selectIndex == lastPageIndex) {
            return
        }
        //如果允许连续滚动，那么在滚动过程中就会更新页码记录
        if (allowContinuousScroll) {
            lastPageIndex = selectIndex
        } else {
            if (!isScrolling) {
                lastPageIndex = selectIndex
            }
        }
        if (isScrolling && !changeSelectInScrolling) {
            return
        }
        if (selectIndex >= 0) {
            pageChangedListener?.onPageSelect(selectIndex)
        }

    }

    /**
     * 获取页面总数
     */
    private fun getTotalPageCount(): Int = if (itemCount <= 0) {
        0
    } else {
        var totalCount: Int = itemCount / onePageSize
        if (itemCount % onePageSize != 0) {
            totalCount++
        }
        totalCount
    }

    /**
     * 根据 offset 获取页面Index
     */
    private fun getPageIndexByOffset(): Int {
        var pageIndex: Int = 0
        if (canScrollVertically()) {
            val pageHeight = getUsableHeight()
            if (offsetY <= 0 || pageHeight <= 0) {
                pageIndex = 0
            } else {
                pageIndex = offsetY / pageHeight
                if (offsetY % pageHeight > pageHeight / 2) {
                    pageIndex++
                }
            }
        } else {
            val pageWidth = getUsableWidth()
            if (offsetX <= 0 || pageWidth <= 0) {
                pageIndex = 0
            } else {
                pageIndex = offsetX / pageWidth
                if (offsetX % pageWidth > pageWidth / 2) {
                    pageIndex++
                }
            }
        }

        return pageIndex
    }

    /**
     * 可用高度
     */
    private fun getUsableHeight(): Int {
        return height - paddingTop - paddingBottom
    }

    /**
     * 可用宽度
     */
    private fun getUsableWidth(): Int {
        return width - paddingLeft - paddingRight
    }

    /**
     * 获取条目显示区域
     *
     * @param [pos] 位置下标
     * @return 显示区域
     */
    private fun getItemFrameByPosition(pos: Int): Rect {
        var rect: Rect? = itemFrames.get(pos)
        if (rect == null) {
            rect = Rect()
            //获取当前View所在页数
            val page = pos / onePageSize
            //计算当前页数左上角的总偏移量
            var offsetX = 0
            var offsetY = 0
            if (canScrollHorizontally()) {
                offsetX += getUsableWidth() * page
            } else {
                offsetY += getUsableHeight() * page
            }
            //根据在当前页面中的位置确定具体偏移量
            val pagePos = pos % onePageSize// 在当前页面中是第几个
            val row = pagePos / columns //获取所在行
            val col = pagePos - (row * columns)//获取所在列

            offsetX += col * itemWidth
            offsetY += row * itemHeight

            rect.left = offsetX
            rect.top = offsetY
            rect.right = offsetX + itemWidth
            rect.bottom = offsetY + itemHeight

            itemFrames.put(pos, rect)
        }
        return rect
    }

    /**
     * 回收和填充布局
     *
     * @param recycler Recycler
     * @param state    State
     * @param isStart  是否从头开始，用于控制View遍历方向，true 为从头到尾，false 为从尾到头
     */
    private fun recycleAndFillItems(recycler: RecyclerView.Recycler, state: RecyclerView.State, isStart: Boolean) {
        if (state.isPreLayout) {
            return
        }
        //计算显示区域区前后多存储一列或则一行
        val displayRect: Rect =
            Rect(offsetX - itemWidth, offsetY - itemHeight, getUsableWidth() + offsetX + itemWidth, getUsableHeight() + offsetY + itemHeight)
        // 对显显示区域进行修正(计算当前显示区域和最大显示区域对交集)
        displayRect.intersect(0, 0, maxScrollX + getUsableWidth(), maxScrollY + getUsableHeight())
        var startPos: Int = getPageIndexByOffset() * onePageSize
        startPos -= onePageSize * 2
        if (startPos < 0) {
            startPos = 0
        }
        var stopPos = startPos + onePageSize * 4
        if (stopPos > itemCount) {
            stopPos = itemCount
        }
        detachAndScrapAttachedViews(recycler)
        if (isStart) {
            for (i in startPos until stopPos) {
                addOrRemove(recycler, displayRect, i)
            }
        } else {
            for (i in stopPos - 1 downTo startPos) {
                addOrRemove(recycler, displayRect, i)
            }
        }
    }

    private fun addOrRemove(recycler: RecyclerView.Recycler, displayRect: Rect, i: Int) {
        val view = recycler.getViewForPosition(i)
        val rect = getItemFrameByPosition(i)
        if (!Rect.intersects(displayRect, rect)) {
            removeAndRecycleView(view, recycler)
        } else {
            addView(view)
            measureChildWithMargins(view, widthUsed, heightUsed)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            layoutDecorated(
                view, rect.left - offsetX + layoutParams.leftMargin + paddingLeft,
                rect.top - offsetY + layoutParams.topMargin + paddingTop,
                rect.right - offsetX - layoutParams.rightMargin + paddingLeft,
                rect.bottom - offsetY - layoutParams.bottomMargin + paddingTop
            )
        }
    }

    /**
     * 获取需要对齐的View
     *
     * @return 需要对齐的View
     */
    fun findSnapView(): View? {
        if (focusedChild != null) {
            return focusedChild
        }
        if (childCount <= 0) {
            return null
        }
        val targetPos = getPageIndexByOffset() * onePageSize
        for (i in 0 until childCount) {
            val childView = getChildAt(i) ?: return null
            if (getPosition(childView) == targetPos) {
                return childView
            }
        }
        return getChildAt(0)
    }

    @OrientationType
    fun setOrientationType(@OrientationType orientation: Int): Int =
        if (this.orientation == orientation || mScrollState != RecyclerView.SCROLL_STATE_IDLE) {
            this.orientation
        } else {
            this.orientation = orientation
            itemFrames.clear()
            val x = offsetX
            val y = offsetY
            offsetX = y / getUsableHeight() * getUsableWidth()
            offsetY = x / getUsableWidth() * getUsableHeight()
            val mx = maxScrollX
            val my = maxScrollY
            maxScrollX = my / getUsableHeight() * getUsableWidth()
            maxScrollY = mx / getUsableWidth() * getUsableHeight()
            this.orientation
        }


    /**
     * 平滑滚动到上一页
     */
    fun smoothPrePage() {
        smoothScrollToPage(getPageIndexByOffset() - 1)
    }

    /**
     * 平滑滚动到下一页
     */
    fun smoothNextPage() {
        smoothScrollToPage(getPageIndexByOffset() + 1)
    }


    /**
     * 平滑滚动到指定页面
     *
     * @param pageIndex 页面下标
     */
    fun smoothScrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= lastPageCount) {
            return
        }
        recyclerView?.let {
            // 如果滚动到页面之间距离过大，先直接滚动到目标页面到临近页面，在使用 smoothScroll 最终滚动到目标
            // 否则在滚动距离很大时，会导致滚动耗费的时间非常长
            val currentPageIndex = getPageIndexByOffset()
            if (abs(pageIndex - currentPageIndex) > 3) {
                if (pageIndex > currentPageIndex) {
                    scrollToPage(pageIndex - 3)
                } else if (pageIndex < currentPageIndex) {
                    scrollToPage(pageIndex + 3)
                }
            }
            //具体执行的滚动
            val smoothScroller = PagerGridSmoothScroller(it)
            val position = pageIndex * onePageSize
            smoothScroller.targetPosition = position
            startSmoothScroll(smoothScroller)
        }
    }

    /**
     * 上一页
     */
    fun prePage() {
        scrollToPage(getPageIndexByOffset() - 1)
    }

    /**
     * 下一页
     */
    fun nextPage() {
        scrollToPage(getPageIndexByOffset() + 1)
    }

    /**
     * 滚动到指定页面
     *
     * @param pageIndex 页面下标
     */
    fun scrollToPage(pageIndex: Int) {
        if (pageIndex < 0 || pageIndex >= lastPageCount) {
            return
        }
        recyclerView?.let {
            var mTargetOffsetXBy = 0
            var mTargetOffsetYBy = 0
            if (canScrollVertically()) {
                mTargetOffsetXBy = 0
                mTargetOffsetYBy = pageIndex * getUsableHeight() - offsetY
            } else {
                mTargetOffsetXBy = pageIndex * getUsableWidth() - offsetX
                mTargetOffsetYBy = 0
            }
            it.scrollBy(mTargetOffsetXBy, mTargetOffsetYBy)
            setPageIndex(pageIndex, false)
        }

    }

    @IntDef(VERTICAL, HORIZONTAL)
    annotation class OrientationType

}