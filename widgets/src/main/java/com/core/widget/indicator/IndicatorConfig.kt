package com.core.widget.indicator

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.core.util.DensityUtil
import com.core.widget.R

/**
 * 指示器配置文件
 * @author like
 * @date 10/13/21 5:23 PM
 */
class IndicatorConfig(private val context: Context) {

    /**
     * 正常宽度
     */
    var normalWidth: Int = DensityUtil.dp2px(context, 8F)

    /**
     * 选中的宽度
     */
    var selectedWidth: Int = DensityUtil.dp2px(context, 9F)

    /**
     * 正常高度
     */
    var normalHeight: Int = DensityUtil.dp2px(context, 8F)

    /**
     * 选中的高度
     */
    var selectedHeight: Int = DensityUtil.dp2px(context, 9F)

    /**
     * 圆角
     */
    var radius:Int = DensityUtil.dp2px(context, 2F)

    /**
     * 正常时颜色
     */
    @ColorInt
    var normalColor: Int = ContextCompat.getColor(context, R.color.lk_indicator_color)

    /**
     * 选中时颜色
     */
    @ColorInt
    var selectedColor: Int = ContextCompat.getColor(context, R.color.lk_indicator_selected_color)

    /**
     * 指示器大小
     */
    var indicatorSize: Int = 0

    /**
     * 指示器间距
     */
    var indicatorSpace: Int = DensityUtil.dp2px(context, 8F)

    /**
     * 当前选中下标
     */
    var currentPosition: Int = 0

    /**
     * 显示类型
     */
    @Type
    var type: Int = Type.CIRCLE

    /**
     * 指示器位置
     */
    @Direction
    var gravity = Direction.CENTER

    /**
     * 间距
     */
    var margins: Margins = Margins().apply {
        topMargin = DensityUtil.dp2px(context, 10F)
    }

    @IntDef(Type.CIRCLE, Type.CUSTOM, Type.SCROLL)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Type {
        companion object {
            const val CIRCLE = 0
            const val CUSTOM = 1
            const val SCROLL = 2
        }
    }

    @IntDef(Direction.LEFT, Direction.CENTER, Direction.RIGHT)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Direction {
        companion object {
            const val LEFT = 0
            const val CENTER = 1
            const val RIGHT = 2
        }
    }

    /**
     * 间距
     */
    class Margins {

        var leftMargin = 0

        var topMargin = 0

        var rightMargin = 0

        var bottomMargin = 0
    }

}