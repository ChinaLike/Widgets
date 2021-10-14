package com.core.widget.indicator

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

/**
 * 指示器基类
 * @author like
 * @date 10/13/21 5:34 PM
 */
open class BaseIndicator : View, Indicator {

    protected val config: IndicatorConfig = IndicatorConfig(context)

    protected val paint: Paint = Paint()

    protected var offset: Float = 0F

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        paint.isAntiAlias   = true
        paint.color = Color.TRANSPARENT
        paint.color = config.normalColor
    }

    override fun indicatorView(): View {
        return this
    }

    override fun indicatorConfig(): IndicatorConfig {
        return config
    }

    /**
     * 页面总数量变化
     * @param [pageSize] 总数
     */
    override fun onPageSizeChanged(pageSize: Int) {
        config.indicatorSize = pageSize
        requestLayout()
    }

    /**
     * 页面被选中
     * @param [pageIndex] 选中的下标
     */
    override fun onPageSelect(pageIndex: Int) {
        config.currentPosition = pageIndex
        invalidate()
    }
}