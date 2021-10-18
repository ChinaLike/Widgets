package com.core.widget.pager

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager

/**
 *
 * @author like
 * @date 10/18/21 11:44 AM
 */
class PagerGridLayoutManager : GridLayoutManager {

    var canScrollHorizontally:Boolean  = false

    var canScrollVertically:Boolean = false

    constructor(context: Context, spanCount: Int) : super(context, spanCount)
    constructor(context: Context, spanCount: Int, orientation: Int, reverseLayout: Boolean) : super(context, spanCount, orientation, reverseLayout)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr:Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun canScrollHorizontally(): Boolean {
        return canScrollHorizontally
    }

    override fun canScrollVertically(): Boolean {
        return canScrollVertically
    }

}