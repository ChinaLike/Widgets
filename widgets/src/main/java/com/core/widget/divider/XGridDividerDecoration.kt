package com.core.widget.divider

import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 网格的分割线
 * @author like
 * @date 2020/12/30 10:28 AM
 */
class XGridDividerDecoration : RecyclerView.ItemDecoration {

    private var horizontalSpace: Int = 0

    private var verticalSpace: Int = 0

    /**
     * 颜色，默认透明
     */
    private var color: Int = Color.TRANSPARENT

    constructor() : this(1)

    constructor(space: Int) : this(space, Color.TRANSPARENT)

    constructor(space: Int, color: Int) {
        this.horizontalSpace = space
        this.verticalSpace = space
        this.color = color
    }

    constructor(horizontalSpace: Int, verticalSpace: Int, color: Int) {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        this.color = color
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (val manager = parent.layoutManager) {
            is GridLayoutManager -> {
                handleDecoration(parent, manager.spanCount, view, outRect)
            }
            is StaggeredGridLayoutManager -> {
                handleDecoration(parent, manager.spanCount, view, outRect)
            }
            else -> {
                super.getItemOffsets(outRect, view, parent, state)
            }
        }
    }

    private fun handleDecoration(
        parent: RecyclerView,
        span: Int,
        view: View,
        outRect: Rect
    ) {
        //为了Item大小均匀，将设定分割线平均分给左右两边Item各一半
        val horizontalOffset = horizontalSpace / 2
        val childPosition = parent.getChildAdapterPosition(view)
        //第一排，顶部不画
        if (childPosition < span) {
            when {
                childPosition % span == 0 -> {
                    //最左边的，左边不画
                    outRect.set(0, 0, horizontalOffset, 0)
                }
                childPosition % span == span - 1 -> {
                    //最右边，右边不画
                    outRect.set(horizontalOffset, 0, 0, 0)
                }
                else -> {
                    outRect.set(horizontalOffset, 0, horizontalOffset, 0)
                }
            }
        } else {
            //上下的分割线，就从第二排开始，每个区域的顶部直接添加设定大小，不用再均分了
            when {
                childPosition % span == 0 -> {
                    outRect.set(0, verticalSpace, horizontalOffset, 0);
                }
                childPosition % span == span - 1 -> {
                    outRect.set(horizontalOffset, verticalSpace, 0, 0);
                }
                else -> {
                    outRect.set(horizontalOffset, verticalSpace, horizontalOffset, 0);
                }
            }
        }
    }


}