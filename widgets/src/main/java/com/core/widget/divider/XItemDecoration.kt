package com.core.widget.divider

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * RecyclerView的分割线
 * 1.支持自定义Drawable类型
 * 2.支持LinearLayoutManager前后缩进（前后一致）
 * 3.支持水平和垂直分割线
 * 4.支持是否绘制最后Item后的一根分割线
 * 5.支持LinearLayoutManager的reverseLayout为true的情况，其他不支持
 * 6.自定义边框宽度（导致每个Item大小不一致）
 * 7.支持LinearLayoutManager的orientation为RecyclerView.HORIZONTAL的情况，其他不支持
 * @author like
 * @date 2020/12/30 10:28 AM
 */
class XItemDecoration : RecyclerView.ItemDecoration {

    /**
     * 布局滚动方向（默认垂直方向）
     */
    private var orientation: Int = RecyclerView.VERTICAL

    /**
     * 布局反向
     */
    private var reverseLayout: Boolean = false

    /**
     * 水平分割线大小
     */
    private var horizontalSpace: Int = 1

    /**
     * 垂直分割线大小
     */
    private var verticalSpace: Int = 1

    /**
     * 自定义Drawable
     */
    private var dividerDrawable: Drawable? = null

    private var isDrawable: Boolean = false

    /**
     * 画笔
     */
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    /**
     * 显示边框
     */
    var showBorder: Boolean = false

    /**
     * 是否绘制最后Item的底部分割线(针对LinearLayoutManager有用)
     */
    var isDrawLastDivider: Boolean = false

    /**
     * 水平边框大小
     */
    var horizontalBorderSize: Int = -1

    /**
     * 垂直边框大小
     */
    var verticalBorderSize: Int = -1

    /**
     * 缩进值(只支持LinearLayoutManager)
     */
    var inset: Int = 0


    constructor(drawable: Drawable? = null) {
        this.isDrawable = drawable != null
        this.dividerDrawable = drawable
    }

    @JvmOverloads
    constructor(space: Int, @ColorInt color: Int = Color.TRANSPARENT) {
        this.horizontalSpace = space
        this.verticalSpace = space
        initPaint(color)
    }

    constructor(horizontalSpace: Int, verticalSpace: Int, @ColorInt color: Int = Color.TRANSPARENT) {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        initPaint(color)
    }

    /**
     * 初始化画笔
     */
    private fun initPaint(@ColorInt color: Int) {
        paint.color = color
        paint.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutParams = view.layoutParams as? RecyclerView.LayoutParams ?: return
        //当前位置
        val position = layoutParams.viewLayoutPosition
        val childCount = parent.adapter?.itemCount ?: 0
        horizontalSpace = if (dividerDrawable != null) dividerDrawable!!.intrinsicHeight else horizontalSpace
        verticalSpace = if (dividerDrawable != null) dividerDrawable!!.intrinsicWidth else verticalSpace

        when (val manager = parent.layoutManager) {
            is GridLayoutManager -> {
                inset = 0
                orientation = manager.orientation
                reverseLayout = manager.reverseLayout
                val spanCount = getSpanCount(parent)
                val spanSize: Int = manager.spanSizeLookup.getSpanSize(position)
                val spanIndex: Int = manager.spanSizeLookup.getSpanIndex(position, spanCount)

                if (reverseLayout) {
                    throw IllegalArgumentException("不支持reverseLayout为true的情况")
                    return
                }
                //第一行
                val isFirstRow = isFirstRow(parent, position, spanCount)
                //最后一行
                val isLastRow = isLastRow(parent, position, spanCount, childCount)
                //第一列
                val isFirstColumn = isFirstColumn(parent, position, spanCount)
                //最后一列
                val isLastColumn = isLastColumn(parent, position, spanCount, childCount)
                if (orientation == RecyclerView.VERTICAL) {
                    if (showBorder) {
                        verticalBorderSize = if (verticalBorderSize != -1) verticalBorderSize else verticalSpace
                        horizontalBorderSize = if (horizontalBorderSize != -1) horizontalBorderSize else horizontalSpace

                        val allDividerWidth: Int = (spanCount - 1) * verticalSpace + 2 * verticalBorderSize
                        verticalOutRect(
                            outRect, spanCount, spanSize, spanIndex, isLastRow, allDividerWidth, isLastColumn, isFirstRow && showBorder
                        )
                    } else {
                        val allDividerWidth: Int = (spanCount - 1) * verticalSpace
                        verticalOutRect(
                            outRect, spanCount, spanSize, spanIndex, isLastRow, allDividerWidth, isLastColumn, isFirstRow && showBorder
                        )
                    }
                } else {
                    if (showBorder) {
                        verticalBorderSize = if (verticalBorderSize != -1) verticalBorderSize else verticalSpace
                        horizontalBorderSize = if (horizontalBorderSize != -1) horizontalBorderSize else horizontalSpace

                        val allDividerWidth: Int = (spanCount - 1) * horizontalSpace + 2 * horizontalBorderSize
                        horizontalOutRect(
                            outRect, spanCount, spanSize, spanIndex, isLastRow, allDividerWidth, isLastColumn, isFirstColumn && showBorder
                        )
                    } else {
                        val allDividerWidth: Int = (spanCount - 1) * horizontalSpace
                        horizontalOutRect(
                            outRect, spanCount, spanSize, spanIndex, isLastRow, allDividerWidth, isLastColumn, isFirstColumn && showBorder
                        )
                    }
                }
            }
            is LinearLayoutManager -> {
                orientation = manager.orientation
                reverseLayout = manager.reverseLayout
                if (reverseLayout) {
                    //布局反向
                    if (isDrawLastDivider || position != (childCount - 1)) {
                        val isVertical = orientation == RecyclerView.VERTICAL
                        outRect.set(if (isVertical) 0 else verticalSpace, if (isVertical) horizontalSpace else 0, 0, 0)
                    }
                } else {
                    if (isDrawLastDivider || position != (childCount - 1)) {
                        val isVertical = orientation == RecyclerView.VERTICAL
                        outRect.set(0, 0, if (isVertical) 0 else verticalSpace, if (isVertical) horizontalSpace else 0)
                    }
                }

            }
            else -> {
                super.getItemOffsets(outRect, view, parent, state)
            }
        }
    }

    private fun verticalOutRect(
        outRect: Rect,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        isLastRow: Boolean,
        allDividerWidth: Int,
        isLastColumn: Boolean,
        isDrawableFirstRow: Boolean
    ) {

        val itemDividerWidth = allDividerWidth / spanCount
        val left = spanIndex * (verticalSpace - itemDividerWidth) + if (showBorder) verticalBorderSize else 0
        var right = itemDividerWidth - left
        if (spanSize != 1) {
            right =
                itemDividerWidth - ((spanIndex + spanSize - 1) * (verticalSpace - itemDividerWidth) + (if (showBorder) verticalBorderSize else 0))
        }
        if (isLastColumn) {
            right = if (showBorder) {
                verticalBorderSize
            } else {
                0
            }
        }
        val top = if (isDrawableFirstRow) horizontalBorderSize else 0
        val bottom = if (isLastRow) if (showBorder) horizontalBorderSize else 0 else horizontalSpace

        outRect.set(left, top, right, bottom)
    }

    private fun horizontalOutRect(
        outRect: Rect,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        isLastRow: Boolean,
        allDividerWidth: Int,
        isLastColumn: Boolean,
        isDrawableFirstColumn: Boolean
    ) {
        val itemDividerWidth = allDividerWidth / spanCount
        val left = if (isDrawableFirstColumn) verticalSpace else 0
        val right = if (isLastColumn) if (showBorder) horizontalBorderSize else 0 else verticalSpace
        val top = spanIndex * (horizontalSpace - itemDividerWidth) + if (showBorder) horizontalBorderSize else 0
        var bottom = itemDividerWidth - top
        if (spanSize != 1) {
            bottom =
                itemDividerWidth - ((spanIndex + spanSize - 1) * (horizontalSpace - itemDividerWidth) + (if (showBorder) horizontalBorderSize else 0))
        }
        if (isLastRow) {
            bottom = if (showBorder) {
                horizontalBorderSize
            } else {
                0
            }
        }
        outRect.set(left, top, right, bottom)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (parent.layoutManager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> {
                inset = 0
                drawVertical(c, parent)
                drawHorizontal(c, parent)
                if (showBorder) {
                    drawBorder(c, parent)
                }
            }
            else -> {
                if (orientation == RecyclerView.VERTICAL) {
                    drawHorizontal(c, parent)
                } else {
                    drawVertical(c, parent)
                }
            }
        }
    }

    /**
     * 绘制横向分割线
     * @param [canvas] 画布
     * @param [parent] 父容器
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val x = parent.paddingLeft + inset
        val width = parent.measuredWidth - parent.paddingRight - inset
        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
            var y: Int = 0
            var height: Int = 0
            if (reverseLayout) {
                height = childView.top - layoutParams.topMargin
                y = height - horizontalSpace
            } else {
                y = childView.bottom + layoutParams.bottomMargin
                height = y + horizontalSpace
            }
            val position = layoutParams.viewLayoutPosition
            val spanCount = getSpanCount(parent)
            if (parent.layoutManager is LinearLayoutManager && isDrawLastDivider) {
                drawDivider(canvas, x, y, width, height)
            } else {
                if (!isLastRow(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
                    drawDivider(canvas, x, y, width, height)
                }
            }
        }
//        for (i in 0 until parent.childCount) {
//            val childView = parent.getChildAt(i)
//            val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
//
//            val left = childView.left - layoutParams.leftMargin
//            val right = left + childView.measuredWidth
//            val top = childView.bottom - layoutParams.bottomMargin
//            val  bottom = top + horizontalSpace
//
//
//            val position = layoutParams.viewLayoutPosition
//            val spanCount = getSpanCount(parent)
//            if (parent.layoutManager is LinearLayoutManager && isDrawLastDivider) {
//                drawDivider(canvas, left, top, right, bottom)
//            } else {
//                if (!isLastRow(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
//                    drawDivider(canvas, left, top, right, bottom)
//                }
//            }
//        }
    }

    /**
     * 绘制竖向分割线
     * @param [canvas] 画布
     * @param [parent] 父容器
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val top = parent.paddingTop + inset
        val bottom = parent.measuredHeight - parent.paddingBottom - inset
        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
            var left: Int = 0
            var right: Int = 0
            if (reverseLayout) {
                right = childView.left - layoutParams.leftMargin
                left = right - verticalSpace
            } else {
                left = childView.right + layoutParams.rightMargin
                right = left + verticalSpace
            }
            val position = layoutParams.viewLayoutPosition
            val spanCount = getSpanCount(parent)
            if (parent.layoutManager is LinearLayoutManager && isDrawLastDivider) {
                drawDivider(canvas, left, top, right, bottom)
            } else {
                if (!isLastColumn(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
                    drawDivider(canvas, left, top, right, bottom)
                }
            }
        }

//        for (i in 0 until parent.childCount) {
//            val childView = parent.getChildAt(i)
//            val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
//
//            val left = childView.right + layoutParams.rightMargin
//            val right = left + verticalSpace
//            val top = childView.top - layoutParams.topMargin
//            val  bottom = top + childView.measuredHeight
//
//
//            val position = layoutParams.viewLayoutPosition
//            val spanCount = getSpanCount(parent)
//            if (parent.layoutManager is LinearLayoutManager && isDrawLastDivider) {
//                drawDivider(canvas, left, top, right, bottom)
//            } else {
//                if (!isLastColumn(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
//                    drawDivider(canvas, left, top, right, bottom)
//                }
//            }
//        }

    }

    /**
     * 绘制边框
     */
    private fun drawBorder(canvas: Canvas, parent: RecyclerView) {
        for (i in 0 until parent.childCount) {
            val childView = parent.getChildAt(i)
            val layoutParams = childView.layoutParams as RecyclerView.LayoutParams
            val position = layoutParams.viewLayoutPosition
            val spanCount = getSpanCount(parent)
            if (isFirstRow(parent, position, spanCount)) {
                val x = parent.paddingLeft
                val width = parent.measuredWidth - parent.paddingRight
                val y = childView.top - layoutParams.topMargin
                val height = y - horizontalBorderSize
                drawDivider(canvas, x, height, width, y)
            } else if (isLastRow(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
                val x = parent.paddingLeft
                val width = parent.measuredWidth - parent.paddingRight
                val y = childView.bottom + layoutParams.bottomMargin
                val height = y + horizontalBorderSize
                drawDivider(canvas, x, y, width, height)
            }

            if (isFirstColumn(parent, position, spanCount)) {
                val top = parent.paddingTop
                val bottom = parent.measuredHeight - parent.paddingBottom
                val right = childView.left - layoutParams.leftMargin
                val left = right - verticalBorderSize
                drawDivider(canvas, left, top, right, bottom)
            } else if (isLastColumn(parent, position, spanCount, parent.adapter?.itemCount ?: 0)) {
                val top = parent.paddingTop
                val bottom = parent.measuredHeight - parent.paddingBottom
                val left = childView.right + layoutParams.rightMargin
                val right = left + verticalBorderSize
                drawDivider(canvas, left, top, right, bottom)
            }
        }
    }

    /**
     * 绘制分割线
     */
    private fun drawDivider(canvas: Canvas, left: Int, top: Int, right: Int, bottom: Int) {
        if (dividerDrawable != null) {
            dividerDrawable?.setBounds(left, top, right, bottom)
            dividerDrawable?.draw(canvas)
        } else {
            canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }

    /**
     * 获取网格数
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }

    /**
     * 是否最后一列
     * @param [parent] 列表组件
     * @param [position] 当前下标位置
     * @param [spanCount] 栅格数
     * @param [childCount] 布局总数
     */
    private fun isLastColumn(parent: RecyclerView, position: Int, spanCount: Int, childCount: Int): Boolean {
        when (parent.layoutManager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) (position + 1) % spanCount == 0 else position >= (childCount - childCount % spanCount)
            }
            is LinearLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) true else position >= childCount
            }
        }
        return false
    }

    /**
     * 是否第一列
     * @param [parent] 列表组件
     * @param [position] 当前下标位置
     * @param [spanCount] 栅格数
     */
    private fun isFirstColumn(parent: RecyclerView, position: Int, spanCount: Int): Boolean {
        when (parent.layoutManager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) position % spanCount == 0 else position < spanCount
            }
            is LinearLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) true else position == 0
            }
        }
        return false
    }

    /**
     * 最后一行
     * @param [parent] 列表组件
     * @param [position] 当前下标位置
     * @param [spanCount] 栅格数
     * @param [childCount] 布局总数
     */
    private fun isLastRow(parent: RecyclerView, position: Int, spanCount: Int, childCount: Int): Boolean {
        when (parent.layoutManager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) position >= (childCount - childCount % spanCount) else (position + 1) % spanCount == 0
            }
            is LinearLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) position >= childCount else true
            }
        }
        return false
    }

    /**
     * 第一行
     * @param [parent] 列表组件
     * @param [position] 当前下标位置
     * @param [spanCount] 栅格数
     */
    private fun isFirstRow(parent: RecyclerView, position: Int, spanCount: Int): Boolean {
        when (parent.layoutManager) {
            is GridLayoutManager, is StaggeredGridLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) position < spanCount else position % spanCount == 0
            }
            is LinearLayoutManager -> {
                return if (orientation == RecyclerView.VERTICAL) position == 0 else true
            }
        }
        return false
    }

}