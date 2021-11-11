package com.core.widget.divider

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * 列表分割线帮助类
 * @author like
 * @date 11/10/21 4:56 PM
 */
internal class LinearItemDecoration : RecyclerItemDecoration() {

    private var layoutManager: LinearLayoutManager? = null

    /**
     * 布局反向
     */
    private var reverseLayout: Boolean = false

    /**
     * 分割线大小
     */
    var space: Int = DEFAULT_DIVIDER_SPACE

    /**
     * 缩进值
     */
    var inset: Int = 0

    /**
     * 设置分割线
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        layoutManager = parent.layoutManager as? LinearLayoutManager

        val viewLayoutPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        val itemCount = parent.adapter!!.itemCount

        val firstCol: Boolean = isFirstCol(viewLayoutPosition)
        val firstRow: Boolean = isFirstRow(viewLayoutPosition)
        val lastCol: Boolean = isLastCol(itemCount, viewLayoutPosition)
        val lastRow: Boolean = isLastRow(itemCount, viewLayoutPosition)

        val isDrawFirstCol = firstCol && drawFirstColumn
        val isDrawFirstRow = firstRow && drawFirstRow
        val isDrawLastRow = lastRow && drawLastRow
        val isDrawLastCol = lastCol && drawLastColumn

        if (lastRow) {
            if (lastCol) {
                // 最后一行最后一列，判断是否绘制右边和底部
                outRect.set(
                    if (isDrawFirstCol) leftColumnSpace ?: space else 0, if (isDrawFirstRow) topRowSpace ?: space else 0,
                    if (isDrawLastCol) rightColumnSpace ?: space else 0, if (isDrawLastRow) bottomRowSpace ?: space else 0
                )
            } else {
                // 最后一行但是不是最后一列，判断是否绘制底部，右边一定需要绘制
                outRect.set(
                    if (isDrawFirstCol) leftColumnSpace ?: space else 0, if (isDrawFirstRow) topRowSpace ?: space else 0,
                    space, if (isDrawLastRow) bottomRowSpace ?: space else 0
                )
            }
        } else if (lastCol) {
            if (lastRow) {
                // 最后一列最后一行，判断是否绘制右边和底部
                outRect.set(
                    if (isDrawFirstCol) leftColumnSpace ?: space else 0, if (isDrawFirstRow) topRowSpace ?: space else 0,
                    if (isDrawLastCol) rightColumnSpace ?: space else 0, if (isDrawLastRow) bottomRowSpace ?: space else 0
                )
            } else {
                // 最后一列但是不是最后一行，判断是否绘制右边，底部一定需要绘制
                outRect.set(
                    if (isDrawFirstCol) leftColumnSpace ?: space else 0, if (isDrawFirstRow) topRowSpace ?: space else 0,
                    if (isDrawLastCol) rightColumnSpace ?: space else 0, space
                )
            }
        } else {
            // 不是最后一行也不是最后一列，绘制右边和底部
            outRect.set(
                if (isDrawFirstCol) leftColumnSpace ?: space else 0, if (isDrawFirstRow) topRowSpace ?: space else 0,
                space, space
            )
        }
    }

    /**
     * 绘制分割线
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCountTotal = parent.adapter!!.itemCount
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childAt = parent.getChildAt(i)
            val viewLayoutPosition = (childAt.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
            val layoutParams = childAt.layoutParams as RecyclerView.LayoutParams

            // --------------------【start】 绘制第一行和第一列之前的线 【start】--------------------//
            val firstRow = isFirstRow(viewLayoutPosition)
            val firstCol = isFirstCol(viewLayoutPosition)
            if (firstRow && drawFirstRow) {
                // 第一行，并且需要绘制第一行之前的分割线
                val left = childAt.left - layoutParams.leftMargin
                val right = childAt.right + layoutParams.rightMargin
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: space)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c,left,top,right,bottom,topRowDrawable,topRowColor)
            }
            if (firstCol && drawFirstColumn) {
                // 第一列，并且需要绘制第一列之前的分割线
                val left: Int = childAt.left - layoutParams.leftMargin - (leftColumnSpace ?: space)
                val right = childAt.left - layoutParams.leftMargin
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin
                drawDivider(c,left,top,right,bottom,leftColumnDrawable,leftColumnColor)
            }
            // ----------------------【end】 绘制第一行和第一列之前的线 【end】----------------------//


            // -----------【start】 绘制中间分割线以及最后一行和最后一列之后的线 【start】------------//
            /**
             * 在此处如果不在对最后一行或者最后一列进行判断的话，
             * 当在页面中给整个 RecyclerView 控件设置 padding 值时，依然会将最后一行的底部和最后一列的右边绘制出来
             */
            val lastRaw = isLastRow(childCountTotal, viewLayoutPosition)
            val lastCol = isLastCol(childCountTotal, viewLayoutPosition)
            if (lastRaw && drawLastRow) {
                // 是最后一行，并且需要绘制最后一行水平方向行的线
                val left = childAt.left - layoutParams.leftMargin
                val right = childAt.right + layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: space)
                drawDivider(c,left,top,right,bottom,bottomRowDrawable,bottomRowColor)
            } else {
                // 不是最后一行，画水平方向的线
                val left = childAt.left - layoutParams.leftMargin
                val right = childAt.right + layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + space
                drawDivider(c,left,top,right,bottom,dividerDrawable,dividerColor)
            }
            if (lastCol && drawLastColumn) {
                // 是最后一列，并且需要绘制最后一列垂直方向行的线
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: space)
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin
                drawDivider(c,left,top,right,bottom,rightColumnDrawable,rightColumnColor)
            } else {
                // 不是最后一列，画竖直方向的线
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + space
                val top = childAt.top - layoutParams.topMargin
                val bottom = childAt.bottom + layoutParams.bottomMargin
                drawDivider(c,left,top,right,bottom,dividerDrawable,dividerColor)
            }
            // -------------【end】 绘制中间分割线以及最后一行和最后一列之后的线 【end】--------------//


            // -----------------【start】 绘制水平方向和垂直方向交叉点的线 【start】-----------------//

            // 画中间位置水平方向和竖直方向的线的交叉点的背景
            if (!lastRaw && !lastCol) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + space
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + space
                drawDivider(c,left,top,right,bottom,dividerDrawable,dividerColor)
            }

            // -----【start】 绘制除去四个角落(左上、左下、右上、右下)位置的交叉点 【start】--- //
            // 绘制第一行，但是不是最后一列，交叉点的位置需要绘制
            if (firstRow && !lastCol && drawFirstRow) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + space
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: space)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c,left,top,right,bottom,topRowDrawable,topRowColor)
            }

            // 绘制第一列，但是不是最后一行，交叉点的位置需要绘制
            if (firstCol && !lastRaw && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: space)
                val right = childAt.left - layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + space
                drawDivider(c,left,top,right,bottom,leftColumnDrawable,leftColumnColor)
            }

            // 是最后一列，但不是最后一行，交叉点的位置需要绘制
            if (lastCol && !lastRaw && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: space)
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + space
                drawDivider(c,left,top,right,bottom,rightColumnDrawable,leftColumnColor)
            }

            // 是最后一行，但不是最后一列，交叉点的位置需要绘制
            if (lastRaw && !lastCol && drawLastRow) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + space
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + space
                drawDivider(c,left,top,right,bottom,bottomRowDrawable,bottomRowColor)
            }

            // -----【end】 绘制除去四个角落(左上、左下、右上、右下)位置的交叉点 【end】--- //

            // -----【start】 绘制四个角落(左上、左下、右上、右下)位置的交叉点 【start】--- //

            // 左上角位置交叉点
            if (firstRow && firstCol && drawFirstRow && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: space)
                val right = childAt.left - layoutParams.rightMargin
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: space)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c,left,top,right,bottom,topRowDrawable,topRowColor)
            }

            // 右上角位置交叉点
            if (firstRow && lastCol && drawFirstRow && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: space)
                val top: Int = childAt.top - layoutParams.topMargin - (topRowSpace ?: space)
                val bottom = childAt.top - layoutParams.topMargin
                drawDivider(c,left,top,right,bottom,topRowDrawable,topRowColor)
            }

            // 右下角位置交叉点
            if (lastRaw && lastCol && drawLastRow && drawLastColumn) {
                val left = childAt.right + layoutParams.rightMargin
                val right: Int = left + (rightColumnSpace ?: space)
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: space)
                drawDivider(c,left,top,right,bottom,bottomRowDrawable,bottomRowColor)
            }

            // 左下角位置交叉点
            if (lastRaw && firstCol && drawLastRow && drawFirstColumn) {
                val left: Int = childAt.left - layoutParams.rightMargin - (leftColumnSpace ?: space)
                val right = childAt.left - layoutParams.rightMargin
                val top = childAt.bottom + layoutParams.bottomMargin
                val bottom: Int = top + (bottomRowSpace ?: space)
                drawDivider(c,left,top,right,bottom,bottomRowDrawable,bottomRowColor)
            }

            // -----【end】 绘制四个角落(左上、左下、右上、右下)位置的交叉点 【end】--- //

            // --------------------【end】绘制水平方向和垂直方向交叉点的线 【end】-------------------//
        }
    }

    /**
     * 判断是否第一列
     */
    private fun isFirstCol(itemPosition: Int): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            true
        } else {
            itemPosition == 0
        }
    }

    /**
     * 判断是否第一行
     */
    private fun isFirstRow(itemPosition: Int): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            itemPosition == 0
        } else {
            true
        }
    }

    /**
     * 判断是否最后一列
     */
    private fun isLastCol(childCount: Int, itemPosition: Int): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            true
        } else {
            itemPosition + 1 == childCount
        }
    }

    /**
     * 判断是否最后一行
     */
    private fun isLastRow(childCount: Int, itemPosition: Int): Boolean {
        return if (orientation == LinearLayoutManager.VERTICAL) {
            itemPosition + 1 == childCount
        } else {
            true
        }
    }
}