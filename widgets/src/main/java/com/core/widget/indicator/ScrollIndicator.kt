package com.core.widget.indicator

import android.content.Context
import android.util.AttributeSet

/**
 * 滚动指示器
 * @author like
 * @date 10/14/21 1:39 PM
 */
class ScrollIndicator:BaseIndicator {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }
}