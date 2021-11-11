package com.core.widget.divider

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 网格分割线帮助类
 * @author like
 * @date 11/10/21 4:56 PM
 */
internal class GridItemDecoration : RecyclerItemDecoration() {

    /**
     * 水平分割线大小
     */
    var horizontalSpace: Int = DEFAULT_DIVIDER_SPACE

    /**
     * 垂直分割线大小
     */
    var verticalSpace: Int = DEFAULT_DIVIDER_SPACE


    /**
     * 设置分割线
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager
        if (layoutManager !is GridLayoutManager) {
            throw IllegalArgumentException("The RecyclerView.LayoutManager is not GridLayoutManager")
        }

        val spanSizeLookup = layoutManager.spanSizeLookup
        val viewLayoutPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val itemCount = parent.adapter?.itemCount ?: 0
        val spanCount = layoutManager.spanCount
        val spanSize = spanSizeLookup.getSpanSize(viewLayoutPosition)
        val spanIndex = spanSizeLookup.getSpanIndex(viewLayoutPosition, spanCount)

        val firstCol: Boolean = isFirstCol(layoutManager, viewLayoutPosition)
        val firstRow: Boolean = isFirstRow(layoutManager, viewLayoutPosition)
        val lastCol: Boolean = isLastCol(layoutManager, itemCount, viewLayoutPosition)
        val lastRow: Boolean = isLastRow(layoutManager, itemCount, viewLayoutPosition)

        val isDrawFirstColumn = firstCol && drawFirstColumn
        val isDrawFirstRow = firstRow && drawFirstRow

        if (orientation == GridLayoutManager.VERTICAL) {
            if (drawFirstColumn && drawLastColumn) {
                // 第一列和最后一列都绘制
                val allDividerWidth: Int = (spanCount - 1) * verticalSpace + (leftColumnSpace ?: verticalSpace) + (rightColumnSpace ?: verticalSpace)
                verticalOutRect(outRect, spanCount, spanSize, spanIndex, lastRow, isDrawFirstRow, allDividerWidth, lastCol)
            } else if (drawFirstColumn) {
                // 第一列绘制
                val allDividerWidth: Int = (spanCount - 1) * verticalSpace + (leftColumnSpace ?: verticalSpace)
                verticalOutRect(outRect, spanCount, spanSize, spanIndex, lastRow, isDrawFirstRow, allDividerWidth, lastCol)
            } else if (drawLastColumn) {
                // 最后一列绘制
                val allDividerWidth: Int = (spanCount - 1) * verticalSpace + (rightColumnSpace ?: verticalSpace)
                verticalOutRect(outRect, spanCount, spanSize, spanIndex, lastRow, isDrawFirstRow, allDividerWidth, lastCol)
            } else {
                // 第一列和最后一列都不绘制
                val allDividerWidth: Int = (spanCount - 1) * verticalSpace
                verticalOutRect(outRect, spanCount, spanSize, spanIndex, lastRow, isDrawFirstRow, allDividerWidth, lastCol)
            }
        } else {
            if (drawFirstRow && drawLastRow) {
                // 第一列和最后一列都绘制
                val allDividerWidth: Int = (spanCount - 1) * horizontalSpace + (topRowSpace ?: horizontalSpace) + (bottomRowSpace ?: horizontalSpace)
                horizontalOutRect(outRect, spanCount, spanSize, spanIndex, lastCol, lastRow, isDrawFirstColumn, allDividerWidth)
            } else if (drawFirstRow) {
                // 第一列绘制
                val allDividerWidth: Int = (spanCount - 1) * horizontalSpace + (topRowSpace ?: horizontalSpace)
                horizontalOutRect(outRect, spanCount, spanSize, spanIndex, lastCol, lastRow, isDrawFirstColumn, allDividerWidth)
            } else if (drawLastRow) {
                // 最后一列绘制
                val allDividerWidth: Int = (spanCount - 1) * horizontalSpace + (bottomRowSpace ?: horizontalSpace)
                horizontalOutRect(outRect, spanCount, spanSize, spanIndex, lastCol, lastRow, isDrawFirstColumn, allDividerWidth)
            } else {
                // 第一列和最后一列都不绘制
                val allDividerWidth: Int = (spanCount - 1) * horizontalSpace
                horizontalOutRect(outRect, spanCount, spanSize, spanIndex, lastCol, lastRow, isDrawFirstColumn, allDividerWidth)
            }
        }
    }

    /**
     * 绘制分割线
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager as? GridLayoutManager
        if (layoutManager !is GridLayoutManager) {
            throw IllegalArgumentException("The RecyclerView.LayoutManager is not GridLayoutManager")
        }
        val childCountTotal = parent.adapter!!.itemCount
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childAt = parent.getChildAt(i)
            val viewLayoutPosition = (childAt.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            val layoutParams = childAt.layoutParams as RecyclerView.LayoutParams

            // --------------------【start】 绘制第一行和第一列之前的线 【start】--------------------//
            val firstRow = isFirstRow(layoutManager, viewLayoutPosition)
            val firstCol = isFirstCol(layoutManager, viewLayoutPosition)
            if (firstRow) {
                // 第一行，并且需要绘制第一行之前的分割线
                val left = childAt.left - layoutParams.leftMargin
                val right = childAt.right + layoutParams.rightMargin
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: horizontalSpace)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c, left, top, right, bottom, topRowDrawable, topRowColor)
            }
            if (firstCol) {
                // 第一列，并且需要绘制第一列之前的分割线
                val left: Int = childAt.left - layoutParams.leftMargin - (leftColumnSpace ?: verticalSpace)
                val right = childAt.left - layoutParams.leftMargin
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin
                drawDivider(c, left, top, right, bottom, leftColumnDrawable, leftColumnColor)
            }
            // ----------------------【end】 绘制第一行和第一列之前的线 【end】----------------------//


            // -----------【start】 绘制中间分割线以及最后一行和最后一列之后的线 【start】------------//
            /**
             * 在此处如果不在对最后一行或者最后一列进行判断的话，
             * 当在页面中给整个 RecyclerView 控件设置 padding 值时，依然会将最后一行的底部和最后一列的右边绘制出来
             */
            val lastRaw = isLastRow(layoutManager!!, childCountTotal, viewLayoutPosition)
            val lastCol = isLastCol(layoutManager!!, childCountTotal, viewLayoutPosition)
            if (!lastRaw) {
                // 不是最后一行，画水平方向的线
                val left = childAt.left - layoutParams.leftMargin
                val right = childAt.right + layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + horizontalSpace
                drawDivider(c, left, top, right, bottom, dividerDrawable, dividerColor)
            } else {
                // 是最后一行，判断是否需要绘制最后一行水平方向行的线
                if (drawLastRow) {
                    val left = childAt.left - layoutParams.leftMargin
                    val right = childAt.right + layoutParams.rightMargin
                    val top = childAt.bottom + layoutParams.bottomMargin
                    val bottom: Int = top + (bottomRowSpace ?: horizontalSpace)
                    drawDivider(c, left, top, right, bottom, bottomRowDrawable, bottomRowColor)
                }
            }
            if (!lastCol) {
                // 不是最后一列，画竖直方向的线
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + verticalSpace
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin
                drawDivider(c, left, top, right, bottom, dividerDrawable, dividerColor)
            } else {
                // 是最后一列，判断是否需要绘制最后一列垂直方向行的线
                if (drawLastColumn) {
                    val left = childAt.right + layoutParams.rightMargin
                    val right: Int = left + (rightColumnSpace ?: verticalSpace)
                    val top = childAt.top - layoutParams.topMargin
                    val bottom = childAt.bottom + layoutParams.bottomMargin
                    drawDivider(c, left, top, right, bottom, rightColumnDrawable, rightColumnColor)
                }
            }
            // -------------【end】 绘制中间分割线以及最后一行和最后一列之后的线 【end】--------------//


            // -----------------【start】 绘制水平方向和垂直方向交叉点的线 【start】-----------------//

            // 画中间位置水平方向和竖直方向的线的交叉点的背景
            if (!lastRaw && !lastCol) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + verticalSpace
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + horizontalSpace
                drawDivider(c, left, top, right, bottom, dividerDrawable, dividerColor)
            }

            // -----【start】 绘制除去四个角落(左上、左下、右上、右下)位置的交叉点 【start】--- //
            // 绘制第一行，但是不是最后一列，交叉点的位置需要绘制
            if (firstRow && !lastCol && drawFirstRow) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + verticalSpace
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: horizontalSpace)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c, left, top, right, bottom, topRowDrawable, topRowColor)
            }

            // 绘制第一列，但是不是最后一行，交叉点的位置需要绘制
            if (firstCol && !lastRaw && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: verticalSpace)
                val right = childAt.left - layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + horizontalSpace
                drawDivider(c, left, top, right, bottom, leftColumnDrawable, leftColumnColor)
            }

            // 是最后一列，但不是最后一行，交叉点的位置需要绘制
            if (lastCol && !lastRaw && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: verticalSpace)
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + horizontalSpace
                drawDivider(c, left, top, right, bottom, rightColumnDrawable, rightColumnColor)
            }

            // 是最后一行，但不是最后一列，交叉点的位置需要绘制
            if (lastRaw && !lastCol && drawLastRow) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + verticalSpace
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: horizontalSpace)
                drawDivider(c, left, top, right, bottom, bottomRowDrawable, bottomRowColor)
            }

            // -----【end】 绘制除去四个角落(左上、左下、右上、右下)位置的交叉点 【end】--- //

            // -----【start】 绘制四个角落(左上、左下、右上、右下)位置的交叉点 【start】--- //

            // 左上角位置交叉点
            if (firstRow && firstCol && drawFirstRow && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: verticalSpace)
                val right = childAt.left - layoutParams.rightMargin
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: horizontalSpace)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c, left, top, right, bottom, topRowDrawable, topRowColor)
            }

            // 右上角位置交叉点
            if (firstRow && lastCol && drawFirstRow && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: verticalSpace)
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: horizontalSpace)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c, left, top, right, bottom, topRowDrawable, topRowColor)
            }

            // 右下角位置交叉点
            if (lastRaw && lastCol && drawLastRow && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: verticalSpace)
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: horizontalSpace)
                drawDivider(c, left, top, right, bottom, bottomRowDrawable, bottomRowColor)
            }

            // 左下角位置交叉点
            if (lastRaw && firstCol && drawLastRow && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: verticalSpace)
                val right = childAt.left - layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: horizontalSpace)
                drawDivider(c, left, top, right, bottom, bottomRowDrawable, bottomRowColor)
            }

            // -----【end】 绘制四个角落(左上、左下、右上、右下)位置的交叉点 【end】--- //

            // --------------------【end】绘制水平方向和垂直方向交叉点的线 【end】-------------------//
        }
    }

    private fun horizontalOutRect(
        outRect: Rect,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        lastCol: Boolean,
        lastRow: Boolean,
        isDrawFirstColumn: Boolean,
        allDividerWidth: Int
    ) {
        // 计算每个item需要移动的宽度
        val itemDividerWidth = allDividerWidth / spanCount
        val left = if (isDrawFirstColumn) (leftColumnSpace ?: verticalSpace) else 0
        val right = if (lastCol) if (drawLastColumn) (rightColumnSpace ?: verticalSpace) else 0 else verticalSpace
        val top: Int = spanIndex * (horizontalSpace - itemDividerWidth) + if (drawFirstRow) (topRowSpace ?: horizontalSpace) else 0
        var bottom = itemDividerWidth - top
        // 主要是对 调用了 GridLayoutManager#setSpanSizeLookup(SpanSizeLookup) 方法的 GridLayoutManager 进行处理
        if (spanSize != 1) bottom =
            itemDividerWidth - ((spanIndex + spanSize - 1) * (horizontalSpace - itemDividerWidth) + if (drawFirstRow) (topRowSpace
                ?: horizontalSpace) else 0)
        if (lastRow) {
            if (drawLastRow) bottom = (bottomRowSpace ?: horizontalSpace) else bottom = 0
        }
        outRect[left, top, right] = bottom
    }

    private fun verticalOutRect(
        outRect: Rect,
        spanCount: Int,
        spanSize: Int,
        spanIndex: Int,
        lastRow: Boolean,
        isDrawFirstRow: Boolean,
        allDividerWidth: Int,
        lastCol: Boolean
    ) {
        // 计算每个item需要移动的宽度
        val itemDividerWidth = allDividerWidth / spanCount
        val left: Int = spanIndex * (verticalSpace - itemDividerWidth) + if (drawFirstColumn) (leftColumnSpace ?: verticalSpace) else 0
        var right = itemDividerWidth - left
        // 主要是对 调用了 GridLayoutManager#setSpanSizeLookup(SpanSizeLookup) 方法的 GridLayoutManager 进行处理
        if (spanSize != 1) right =
            itemDividerWidth - ((spanIndex + spanSize - 1) * (verticalSpace - itemDividerWidth) + if (drawFirstColumn) (leftColumnSpace
                ?: verticalSpace) else 0)
        if (lastCol) {
            if (drawLastColumn) right = (rightColumnSpace ?: verticalSpace) else right = 0
        }
        val top = if (isDrawFirstRow) (topRowSpace ?: horizontalSpace) else 0
        val bottom = if (lastRow) if (drawLastRow) (bottomRowSpace ?: horizontalSpace) else 0 else horizontalSpace
        outRect[left, top, right] = bottom
    }

    /**
     * 判断是否第一列
     */
    private fun isFirstCol(layoutManager: GridLayoutManager, itemPosition: Int): Boolean {
        val spanCount = layoutManager.spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup
        val spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount)
        val spanGroupIndex = spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount)
        return if (orientation == GridLayoutManager.VERTICAL) {
            spanIndex == 0
        } else {
            spanGroupIndex == 0
        }
    }

    /**
     * 判断是否第一行
     */
    private fun isFirstRow(layoutManager: GridLayoutManager, itemPosition: Int): Boolean {
        val spanCount = layoutManager.spanCount
        val spanSizeLookup = layoutManager.spanSizeLookup
        val spanGroupIndex = layoutManager.spanSizeLookup.getSpanGroupIndex(itemPosition, spanCount)
        val spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount)
        return if (orientation == GridLayoutManager.VERTICAL) {
            spanGroupIndex == 0
        } else {
            spanIndex == 0
        }
    }

    /**
     * 判断是否最后一列
     */
    private fun isLastCol(layoutManager: GridLayoutManager, childCount: Int, itemPosition: Int): Boolean {
        val spanSizeLookup = layoutManager.spanSizeLookup
        val spanCount = layoutManager.spanCount
        val spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount)
        val spanSize = spanSizeLookup.getSpanSize(itemPosition)
        return if (orientation == GridLayoutManager.VERTICAL) {
            spanIndex + spanSize == spanCount
        } else {
            (childCount - itemPosition) / (spanCount * 1.0f) <= 1
        }
    }

    /**
     * 判断是否最后一行
     */
    private fun isLastRow(layoutManager: GridLayoutManager, childCount: Int, itemPosition: Int): Boolean {
        val spanSizeLookup = layoutManager.spanSizeLookup
        val spanCount = layoutManager.spanCount
        val spanIndex = spanSizeLookup.getSpanIndex(itemPosition, spanCount)
        val spanSize = spanSizeLookup.getSpanSize(itemPosition)
        return if (orientation == GridLayoutManager.VERTICAL) {
            (childCount - itemPosition) / (spanCount * 1.0f) <= 1
        } else {
            spanIndex + spanSize == spanCount
        }
    }
}