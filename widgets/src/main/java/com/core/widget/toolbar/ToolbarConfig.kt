package com.core.widget.toolbar

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.core.widget.R

/**
 * Toolbar全局配置
 * @author like
 * @date 8/31/21 4:46 PM
 */
class ToolbarConfig private constructor(context: Context) {

    /**
     * 主题颜色，主要用于返回按钮颜色，标题颜色，返回文本颜色
     */
    @ColorInt
    var themeColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_default_color)

    /**
     * 返回按钮
     */
    @DrawableRes
    var backIcon: Int = R.drawable.lk_icon_toolbar_back

    /**
     * 返回按钮颜色
     */
    @ColorInt
    var backIconColor: Int? = null

    /**
     * 返回文本颜色
     */
    @ColorInt
    var backTextColor: Int? = null

    /**
     * 返回文本大小
     */
    @Dimension(unit = Dimension.PX)
    var backTextSize: Float = context.resources.getDimension(R.dimen.lk_toolbar_title_text_size)

    /**
     * 返回文本
     */
    var backText: String = "关闭"

    /**
     * 是否显示返回文本
     */
    var showBackText:Boolean = false

    /**
     * 标题大小
     */
    @Dimension(unit = Dimension.PX)
    var titleTextSize: Float = context.resources.getDimension(R.dimen.lk_toolbar_title_text_size)

    /**
     * 标题颜色
     */
    @ColorInt
    var titleTextColor: Int? = null

    /**
     * 标题样式
     */
    var titleTextStyle: Int = Typeface.NORMAL

    /**
     * 标题最多显示个数
     */
    var titleMaxEms:Int = 10

    /**
     * 左内边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarPaddingLeft: Float = context.resources.getDimension(R.dimen.lk_toolbar_horizontal_padding)

    /**
     * 右内边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarPaddingRight: Float = context.resources.getDimension(R.dimen.lk_toolbar_horizontal_padding)

    /**
     * 内容左外边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarContentMarginLeft: Float = context.resources.getDimension(R.dimen.lk_toolbar_middle_layout_margin_horizontal)

    /**
     * 内容右外边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarContentMarginRight: Float = context.resources.getDimension(R.dimen.lk_toolbar_middle_layout_margin_horizontal)

    /**
     * 返回按钮距离返回Icon的距离
     */
    @Dimension(unit = Dimension.PX)
    var backTextMarginLeft: Float = context.resources.getDimension(R.dimen.lk_toolbar_back_text_margin_left)

    /**
     * 是否显示分割线
     */
    var showDivider: Boolean = false

    /**
     * 分割线颜色
     */
    @ColorInt
    var dividerColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_divider_color)

    /**
     * 搜索框高度
     */
    @Dimension(unit = Dimension.PX)
    var searchHeight: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_height)

    /**
     * 搜索框圆角
     */
    @Dimension(unit = Dimension.PX)
    var searchRadius: Float? = null

    /**
     * 搜索框背景
     */
    var searchBackground: Drawable? = null

    /**
     * 搜索框边框宽度
     */
    @Dimension(unit = Dimension.PX)
    var searchStrokeWidth: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_stroke_width)

    /**
     * 搜索框 边线颜色
     */
    @ColorInt
    var searchStrokeColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_search_stroke_color)

    /**
     * 搜索框左边内边距
     */
    @Dimension(unit = Dimension.PX)
    var searchPaddingLeft: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_padding_left)

    /**
     * 搜索框右边内边距
     */
    @Dimension(unit = Dimension.PX)
    var searchPaddingRight: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_padding_right)

    /**
     * 搜索框右边外边距
     */
    @Dimension(unit = Dimension.PX)
    var searchMarginRight: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_margin_right)

    /**
     * 搜索框前边的图标
     */
    var searchBeforeIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.lk_icon_toolbar_search)

    /**
     * 搜索框前面图标的宽度
     */
    @Dimension(unit = Dimension.PX)
    var searchBeforeIconWidth:Float = context.resources.getDimension(R.dimen.lk_toolbar_search_before_icon_width)

    /**
     * 搜索框前面图标的高度
     */
    @Dimension(unit = Dimension.PX)
    var searchBeforeIconHeight:Float = context.resources.getDimension(R.dimen.lk_toolbar_search_before_icon_height)

    /**
     * 搜索框前边的图标颜色
     */
    @ColorInt
    var searchBeforeIconColor: Int? = null

    /**
     * 搜索框距离前面图标的距离
     */
    @Dimension(unit = Dimension.PX)
    var searchEditMarginLeft: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_edit_margin_left)

    /**
     * 搜索框的提示文字
     */
    var searchHintText: String? = ""

    /**
     * 搜索框的提示文字大小
     */
    @Dimension(unit = Dimension.PX)
    var searchHintTextSize: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_hint_text_size)

    /**
     * 搜索框的提示文字颜色
     */
    @ColorInt
    var searchHintTextColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_search_hint_color)

    /**
     * 搜索框的文字
     */
    var searchText: String? = ""

    /**
     * 搜索框的文字大小
     */
    @Dimension(unit = Dimension.PX)
    var searchTextSize: Float = context.resources.getDimension(R.dimen.lk_toolbar_search_text_size)

    /**
     * 搜索框的文字颜色
     */
    @ColorInt
    var searchTextColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_search_text_color)

    /**
     * 操作区域图标
     */
    var operationIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.lk_icon_toolbar_right_search)

    /**
     * 操作图标宽度
     */
    @Dimension(unit = Dimension.PX)
    var operationIconWidth:Float = context.resources.getDimension(R.dimen.lk_toolbar_operation_icon_width)

    /**
     * 操作图标高度
     */
    @Dimension(unit = Dimension.PX)
    var operationIconHeight:Float = context.resources.getDimension(R.dimen.lk_toolbar_operation_icon_height)

    /**
     * 操作区域文字
     */
    var operationText: String? = "取消"

    /**
     * 操作区域文字大小
     */
    @Dimension(unit = Dimension.PX)
    var operationTextSize: Float = context.resources.getDimension(R.dimen.lk_toolbar_operation_text_size)

    /**
     * 操作区域文字颜色
     */
    @ColorInt
    var operationTextColor: Int = ContextCompat.getColor(context, R.color.lk_toolbar_operation_text_color)

    /**
     * 菜单文字大小
     */
    @Dimension(unit = Dimension.PX)
    var menuTextSize:Float = context.resources.getDimension(R.dimen.lk_toolbar_menu_text_size)

    /**
     * 菜单文字颜色
     */
    @ColorInt
    var menuTextColor:Int = ContextCompat.getColor(context, R.color.lk_toolbar_menu_text_color)

    /**
     * 菜单文字位置
     */
    var menuTextLocation:Int = Location.BOTTOM

    /**
     * 文字距离图片
     */
    @Dimension(unit = Dimension.PX)
    var menuTextMarginIcon:Float = context.resources.getDimension(R.dimen.lk_toolbar_menu_text_margin_icon)

    /**
     * 显示搜索框前面的图标
     */
    var showSearchBeforeIcon:Boolean = true

    companion object {

        private lateinit var context: Context

        private val instance: ToolbarConfig by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ToolbarConfig(context)
        }

        fun getInstance(context: Context): ToolbarConfig {
            this.context = context
            return instance
        }

    }

}