package com.core.widget.divider

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * 万能分割线
 * 参考:https://github.com/ChinaLike/RRecyclerView
 * @author like
 * @date 11/10/21 4:52 PM
 */
class XItemDecoration : RecyclerView.ItemDecoration {

    /**
     * 水平分割线大小
     */
    var horizontalSpace: Int = RecyclerItemDecoration.DEFAULT_DIVIDER_SPACE

    /**
     * 垂直分割线大小
     */
    var verticalSpace: Int = RecyclerItemDecoration.DEFAULT_DIVIDER_SPACE

    /**
     * 分割线颜色
     */
    @ColorInt
    var dividerColor: Int = RecyclerItemDecoration.DEFAULT_DIVIDER_COLOR

    /**
     * 自定义Drawable
     */
    var dividerDrawable: Drawable? = null

    /**
     * 分割线帮助类
     */
    private var itemDecoration: RecyclerItemDecoration? = null

    /**
     * 最后一行之后是否绘制分割线
     */
    var isDrawLastRow = false

    /**
     * 最后一列之后是否绘制分割线
     */
    var isDrawLastColumn = false

    /**
     * 第一行之前是否绘制分割线
     */
    var isDrawFirstRow = false

    /**
     * 第一列之前是否绘制分割线
     */
    var isDrawFirstColumn = false

    /**
     * 顶行分割线大小
     */
    var topRowSpace: Int? = null

    /**
     * 尾行分割线大小
     */
    var bottomRowSpace: Int? = null

    /**
     * 左侧分割线大小
     */
    var leftColumnSpace: Int? = null

    /**
     * 右侧分割线大小
     */
    var rightColumnSpace: Int? = null

    /**
     * 顶行分割线颜色
     */
    @ColorInt
    var topRowColor: Int? = null

    /**
     * 顶行分割线
     */
    var topRowDrawable: Drawable? = null

    /**
     * 尾行分割线颜色
     */
    @ColorInt
    var bottomRowColor: Int? = null

    /**
     * 尾行分割线
     */
    var bottomRowDrawable: Drawable? = null

    /**
     * 左侧分割线颜色
     */
    @ColorInt
    var leftColumnColor: Int? = null

    /**
     * 左侧分割线
     */
    var leftColumnDrawable: Drawable? = null

    /**
     * 右侧分割线颜色
     */
    @ColorInt
    var rightColumnColor: Int? = null

    /**
     * 右侧分割线
     */
    var rightColumnDrawable: Drawable? = null

    constructor(drawable: Drawable? = null) {
        this.dividerDrawable = drawable
    }

    @JvmOverloads
    constructor(space: Int, @ColorInt color: Int = RecyclerItemDecoration.DEFAULT_DIVIDER_COLOR) {
        this.horizontalSpace = space
        this.verticalSpace = space
        this.dividerColor = color
    }

    constructor(horizontalSpace: Int, verticalSpace: Int, @ColorInt color: Int = RecyclerItemDecoration.DEFAULT_DIVIDER_COLOR) {
        this.horizontalSpace = horizontalSpace
        this.verticalSpace = verticalSpace
        this.dividerColor = color
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (val manager = parent.layoutManager) {
            is GridLayoutManager -> {
                itemDecoration = GridItemDecoration().apply {
                    orientation = manager.orientation
                    horizontalSpace = this@XItemDecoration.horizontalSpace
                    verticalSpace = this@XItemDecoration.verticalSpace
                }
            }
            is StaggeredGridLayoutManager -> {

            }
            is LinearLayoutManager -> {
                isDrawLastRow = true
                itemDecoration = LinearItemDecoration().apply {
                    orientation = manager.orientation
                    //使用[horizontalSpace] 和 [verticalSpace] 一样
                    space = horizontalSpace
                }
            }
            else -> {
                super.getItemOffsets(outRect, view, parent, state)
            }
        }
        //公共参数
        itemDecoration?.apply {
            dividerColor = this@XItemDecoration.dividerColor
            dividerDrawable = this@XItemDecoration.dividerDrawable
            drawLastRow = this@XItemDecoration.isDrawLastRow
            drawFirstRow = this@XItemDecoration.isDrawFirstRow
            drawFirstColumn = this@XItemDecoration.isDrawFirstColumn
            drawLastColumn = this@XItemDecoration.isDrawLastColumn
            topRowSpace = this@XItemDecoration.topRowSpace
            topRowColor = this@XItemDecoration.topRowColor
            topRowDrawable = this@XItemDecoration.topRowDrawable
            bottomRowSpace = this@XItemDecoration.bottomRowSpace
            bottomRowColor = this@XItemDecoration.bottomRowColor
            bottomRowDrawable = this@XItemDecoration.bottomRowDrawable
            leftColumnSpace = this@XItemDecoration.leftColumnSpace
            leftColumnColor = this@XItemDecoration.leftColumnColor
            leftColumnDrawable = this@XItemDecoration.leftColumnDrawable
            rightColumnSpace = this@XItemDecoration.rightColumnSpace
            rightColumnColor = this@XItemDecoration.rightColumnColor
            rightColumnDrawable = this@XItemDecoration.rightColumnDrawable
            getItemOffsets(outRect, view, parent, state)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (itemDecoration != null) {
            itemDecoration?.onDraw(c, parent, state)
        } else {
            super.onDraw(c, parent, state)
        }
    }
}