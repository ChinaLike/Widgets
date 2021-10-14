package com.core.widget.indicator

import android.content.Context
import android.util.AttributeSet

/**
 * 矩形（条形）指示器
 * 1、可以设置选中和默认的宽度、指示器的圆角
 * 2、如果需要正方形将圆角设置为0，可将宽度和高度设置为一样
 * 3、如果不想选中时变长，可将选中的宽度和默认宽度设置为一样
 *
 * @author like
 * @date 10/14/21 11:32 AM
 */
class RectangleIndicator:BaseIndicator {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }
}