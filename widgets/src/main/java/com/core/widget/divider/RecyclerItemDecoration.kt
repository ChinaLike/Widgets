package com.core.widget.divider

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.recyclerview.widget.RecyclerView

/**
 * 分割线
 * @author like
 * @date 11/10/21 4:58 PM
 */
internal abstract class RecyclerItemDecoration {

    var orientation: Int = RecyclerView.VERTICAL

    /**
     * 分割线颜色
     */
    var dividerColor: Int = DEFAULT_DIVIDER_COLOR

    /**
     * 自定义Drawable
     */
    var dividerDrawable: Drawable? = null

    /**
     * 最后一行之后是否绘制分割线
     */
    var drawLastRow = false

    /**
     * 最后一列之后是否绘制分割线
     */
    var drawLastColumn = false

    /**
     * 第一行之前是否绘制分割线
     */
    var drawFirstRow = false

    /**
     * 第一列之前是否绘制分割线
     */
    var drawFirstColumn = false

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
        get() {
            return field ?: dividerDrawable
        }

    /**
     * 尾行分割线颜色
     */
    @ColorInt
    var bottomRowColor: Int? = null

    /**
     * 尾行分割线
     */
    var bottomRowDrawable: Drawable? = null
        get() {
            return field ?: dividerDrawable
        }

    /**
     * 左侧分割线颜色
     */
    @ColorInt
    var leftColumnColor: Int? = null

    /**
     * 左侧分割线
     */
    var leftColumnDrawable: Drawable? = null
        get() {
            return field ?: dividerDrawable
        }

    /**
     * 右侧分割线颜色
     */
    @ColorInt
    var rightColumnColor: Int? = null

    /**
     * 右侧分割线
     */
    var rightColumnDrawable: Drawable? = null
        get() {
            return field ?: dividerDrawable
        }


    /**
     * 画笔
     */
    protected val paint = Paint(Paint.ANTI_ALIAS_FLAG)


    companion object {

        const val DEFAULT_DIVIDER_SPACE = 1 // 默认宽度

        const val DEFAULT_DIVIDER_COLOR = Color.TRANSPARENT // 默认分割线颜色

    }

    constructor() {
        paint.style = Paint.Style.FILL
        paint.color = DEFAULT_DIVIDER_COLOR
    }

    /**
     * 设置分割线
     */
    abstract fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State)

    /**
     * 绘制分割线
     */
    abstract fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State)

    /**
     * 绘制分割线
     * @param [left] 左
     * @param [top] 上
     * @param [right] 右
     * @param [bottom] 下
     * @param [drawable] 分割线
     * @param [color] 分割线颜色
     */
    internal fun drawDivider(c: Canvas, left: Int, top: Int, right: Int, bottom: Int, drawable: Drawable?, color: Int?) {
        if (drawable != null) {
            drawable?.setBounds(left, top, right, bottom)
            drawable?.draw(c)
        } else {
            paint.color = color ?: dividerColor
            c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
        }
    }
}