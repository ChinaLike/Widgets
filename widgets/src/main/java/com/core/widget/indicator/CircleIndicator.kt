package com.core.widget.indicator

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet

/**
 * 圆点指示器
 * @author like
 * @date 10/13/21 5:41 PM
 */
class CircleIndicator : BaseIndicator {

    /**
     * 正常指示器圆角
     */
    private var mNormalRadius: Int = 0

    /**
     * 选中指示器圆角
     */
    private var mSelectedRadius = 0

    private var maxRadius: Int = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mNormalRadius = config.normalWidth / 2
        mSelectedRadius = config.selectedWidth / 2
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val count = config.indicatorSize
        if (count <= 1) {
            return
        }
        mNormalRadius = config.normalWidth / 2
        mSelectedRadius = config.selectedWidth / 2
        maxRadius = mSelectedRadius.coerceAtLeast(mNormalRadius)
        //  获取总宽度
        val width = (count - 1) * config.indicatorSpace + config.selectedWidth + config.normalWidth * (count - 1)
        setMeasuredDimension(width, config.normalWidth.coerceAtLeast(config.selectedWidth))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val count = config.indicatorSize
        if (count <= 1) {
            return
        }
        var left: Float = 0F
        for (i in 0 until count) {
            paint.color = if (config.currentPosition == i) config.selectedColor else config.normalColor
            val indicatorWidth = if (config.currentPosition == i) config.selectedWidth else config.normalWidth
            val radius = if (config.currentPosition == i) mSelectedRadius else mNormalRadius
            canvas?.drawCircle(left + radius, maxRadius.toFloat(), radius.toFloat(), paint)
            left += indicatorWidth + config.indicatorSpace
        }
    }

}