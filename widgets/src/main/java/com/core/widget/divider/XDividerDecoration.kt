package com.core.widget.divider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.core.util.DensityUtil

/**
 * RecyclerView分割线
 * @author like
 * @date 2020/10/23 2:08 PM
 */
class XDividerDecoration : RecyclerView.ItemDecoration {

    companion object {
        /**
         * 水平
         */
        const val HORIZONTAL = RecyclerView.HORIZONTAL

        /**
         * 垂直
         */
        const val VERTICAL: Int = RecyclerView.VERTICAL

    }


    private var mOrientation: Int = VERTICAL

    /**
     * 缩进值
     */
    private var inset: Int = 0

    private var mPaint: Paint

    /**
     * 线宽
     */
    private var dividerSize: Int = 0

    /**
     * 自定义Drawable
     */
    private var dividerDrawable: Drawable? = null

    private var isDrawable: Boolean = false

    constructor(context: Context) : this(context, VERTICAL)

    constructor(context: Context, orientation: Int) : this(context, orientation, android.R.color.white)

    constructor(context: Context, orientation: Int, @ColorRes colorRes: Int) : this(context, orientation, colorRes, DensityUtil.dp2px(1F))

    constructor(context: Context, orientation: Int, @ColorRes colorRes: Int, size: Int) : this(
        context,
        orientation,
        colorRes,
        size,
        0
    )

    constructor(context: Context, orientation: Int, @ColorRes colorRes: Int, size: Int, inset: Int) {
        this.mOrientation = orientation
        this.dividerSize = size
        this.inset = inset
        this.isDrawable = false
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = ContextCompat.getColor(context, colorRes)
        mPaint?.style = Paint.Style.FILL
    }

    constructor(context: Context, drawable: Drawable) : this(context, VERTICAL, drawable)

    constructor(context: Context, orientation: Int, drawable: Drawable) : this(context, orientation, drawable, 0)

    constructor(context: Context, orientation: Int, drawable: Drawable, inset: Int) {
        this.mOrientation = orientation
        this.dividerDrawable = drawable
        this.inset = inset
        this.isDrawable = true
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint?.color = Color.WHITE
        mPaint?.style = Paint.Style.FILL
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when (mOrientation) {
            VERTICAL -> {
                outRect.set(0, 0, 0, if (dividerDrawable != null) dividerDrawable!!.intrinsicHeight else dividerSize)
            }
            HORIZONTAL -> {
                outRect.set(0, 0, if (dividerDrawable != null) dividerDrawable!!.intrinsicWidth else dividerSize, 0)
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (mOrientation) {
            VERTICAL -> {
                drawVertical(c, parent)
            }
            HORIZONTAL -> {
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        if (!isDrawable) {
            canvas.save()
        }
        //处理用户传递的Drawable
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        //最后一个Item不绘制
        for (i in 0 until parent.childCount) {
            val rootView = parent.getChildAt(i)
            val params = rootView.layoutParams as RecyclerView.LayoutParams
            val top = rootView.bottom + params.bottomMargin
            val bottom = top + if (dividerDrawable != null) dividerDrawable!!.intrinsicHeight else dividerSize
            if (isDrawable) {
                if (inset > 0) {
                    canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
                    dividerDrawable?.setBounds(left + inset, top, right - inset, bottom)
                } else {
                    dividerDrawable?.setBounds(left, top, right, bottom)
                }
                dividerDrawable?.draw(canvas)
            } else {
                canvas.drawRect((left + inset).toFloat(), top.toFloat(), (right - inset).toFloat(), bottom.toFloat(), mPaint)
            }
        }
        if (!isDrawable) {
            canvas.restore()
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        if (!isDrawable) {
            canvas.save()
        }
        //处理用户传递的Drawable
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        //最后一个Item不绘制
        for (i in 0 until parent.childCount) {
            val rootView = parent.getChildAt(i)
            val params = rootView.layoutParams as RecyclerView.LayoutParams
            val left = rootView.right + params.rightMargin
            val right = left + if (dividerDrawable != null) dividerDrawable!!.intrinsicWidth else dividerSize
            if (isDrawable) {
                if (inset > 0) {
                    canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mPaint)
                    dividerDrawable?.setBounds(left, top + inset, right, bottom - inset)
                } else {
                    dividerDrawable?.setBounds(left, top, right, bottom)
                }
                dividerDrawable?.draw(canvas)
            } else {
                canvas.drawRect(left.toFloat(), (top + inset).toFloat(), right.toFloat(), (bottom - inset).toFloat(), mPaint)
            }
        }
        if (!isDrawable) {
            canvas.restore()
        }
    }

    /**
     * 设置画笔颜色
     */
    fun setPaintColor(@ColorInt color:Int){
        mPaint?.color = color
    }

}