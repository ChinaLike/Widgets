package com.core.widget.indicator

import android.content.Context
import android.util.AttributeSet

/**
 *
 * @author like
 * @date 10/14/21 11:34 AM
 */
class RoundLinesIndicator:BaseIndicator {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }
}