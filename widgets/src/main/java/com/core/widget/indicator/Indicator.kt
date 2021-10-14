package com.core.widget.indicator

import android.view.View
import com.core.widget.pager.PageChangedListener

/**
 * 指示器
 * @author like
 * @date 10/13/21 5:27 PM
 */
interface Indicator:PageChangedListener {

    fun indicatorView():View

    fun indicatorConfig():IndicatorConfig

}