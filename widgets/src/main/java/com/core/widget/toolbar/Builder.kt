package com.core.widget.toolbar

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import com.core.widget.R

/**
 * Toolbar参数配置，代码中通过设置这个类的实例给Toolbar来改变样式
 * @author like
 * @date 8/31/21 4:43 PM
 */
internal class Builder constructor(context: Context) {

    private val toolbarConfig = ToolbarConfig.getInstance(context)

    /**
     * 主题颜色，主要用于返回按钮颜色，标题颜色，返回文本颜色
     */
    @ColorInt
    var themeColor: Int = toolbarConfig.themeColor

    /**
     * 返回按钮
     */
    var backIcon: Drawable? = toolbarConfig.backIcon

    /**
     * 返回按钮颜色
     */
    @ColorInt
    var backIconColor: Int? = toolbarConfig.backIconColor

    /**
     * 返回文本颜色
     */
    @ColorInt
    var backTextColor: Int? = toolbarConfig.backTextColor

    /**
     * 返回文本大小
     */
    @Dimension(unit = Dimension.PX)
    var backTextSize: Float = toolbarConfig.backTextSize

    /**
     * 返回文本
     */
    var backText: String = toolbarConfig.backText

    /**
     * 是否显示返回文本
     */
    var showBackText:Boolean = toolbarConfig.showBackText

    /**
     * 标题大小
     */
    @Dimension(unit = Dimension.PX)
    var titleTextSize: Float = toolbarConfig.titleTextSize

    /**
     * 标题颜色
     */
    @ColorInt
    var titleTextColor: Int? = toolbarConfig.titleTextColor

    /**
     * 标题样式
     */
    var titleTextStyle: Int = toolbarConfig.titleTextStyle

    /**
     * 左内边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarPaddingLeft: Float = toolbarConfig.toolbarPaddingLeft

    /**
     * 右内边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarPaddingRight: Float = toolbarConfig.toolbarPaddingRight

    /**
     * 内容左外边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarContentMarginLeft: Float = toolbarConfig.toolbarContentMarginLeft

    /**
     * 内容右外边距
     */
    @Dimension(unit = Dimension.PX)
    var toolbarContentMarginRight: Float = toolbarConfig.toolbarContentMarginRight

    /**
     * 返回按钮距离返回Icon的距离
     */
    @Dimension(unit = Dimension.PX)
    var backTextMarginLeft: Float = toolbarConfig.backTextMarginLeft

    /**
     * 是否显示分割线
     */
    var showDivider: Boolean = toolbarConfig.showDivider

    /**
     * 分割线颜色
     */
    @ColorInt
    var dividerColor: Int = toolbarConfig.dividerColor

    /**
     * 搜索框高度
     */
    @Dimension(unit = Dimension.PX)
    var searchHeight: Float = toolbarConfig.searchHeight

    /**
     * 搜索框圆角
     */
    @Dimension(unit = Dimension.PX)
    var searchRadius: Float? = toolbarConfig.searchRadius

    /**
     * 搜索框背景
     */
    var searchBackground: Drawable? = toolbarConfig.searchBackground

    /**
     * 搜索框边框宽度
     */
    @Dimension(unit = Dimension.PX)
    var searchStrokeWidth: Float = toolbarConfig.searchStrokeWidth

    /**
     * 搜索框 边线颜色
     */
    @ColorInt
    var searchStrokeColor: Int = toolbarConfig.searchStrokeColor

    /**
     * 搜索框左边内边距
     */
    @Dimension(unit = Dimension.PX)
    var searchPaddingLeft: Float = toolbarConfig.searchPaddingLeft

    /**
     * 搜索框右边内边距
     */
    @Dimension(unit = Dimension.PX)
    var searchPaddingRight: Float = toolbarConfig.searchPaddingRight

    /**
     * 搜索框右边外边距
     */
    @Dimension(unit = Dimension.PX)
    var searchMarginRight: Float = toolbarConfig.searchMarginRight

    /**
     * 搜索框前边的图标
     */
    var searchBeforeIcon: Drawable? = toolbarConfig.searchBeforeIcon

    /**
     * 搜索框前面图标的宽度
     */
    @Dimension(unit = Dimension.PX)
    var searchBeforeIconWidth:Float = toolbarConfig.searchBeforeIconWidth

    /**
     * 搜索框前面图标的高度
     */
    @Dimension(unit = Dimension.PX)
    var searchBeforeIconHeight:Float = toolbarConfig.searchBeforeIconHeight

    /**
     * 搜索框前边的图标颜色
     */
    @ColorInt
    var searchBeforeIconColor: Int? = toolbarConfig.searchBeforeIconColor

    /**
     * 搜索框距离前面图标的距离
     */
    @Dimension(unit = Dimension.PX)
    var searchEditMarginLeft: Float = toolbarConfig.searchEditMarginLeft

    /**
     * 搜索框的提示文字
     */
    var searchHintText: String? = toolbarConfig.searchHintText

    /**
     * 搜索框的提示文字大小
     */
    @Dimension(unit = Dimension.PX)
    var searchHintTextSize: Float = toolbarConfig.searchHintTextSize

    /**
     * 搜索框的提示文字颜色
     */
    @ColorInt
    var searchHintTextColor: Int = toolbarConfig.searchHintTextColor

    /**
     * 搜索框的文字
     */
    var searchText: String? = toolbarConfig.searchText

    /**
     * 搜索框的文字大小
     */
    @Dimension(unit = Dimension.PX)
    var searchTextSize: Float = toolbarConfig.searchTextSize

    /**
     * 搜索框的文字颜色
     */
    @ColorInt
    var searchTextColor: Int = toolbarConfig.searchTextColor

    /**
     * 操作区域图标
     */
    var operationIcon: Drawable? = toolbarConfig.operationIcon

    /**
     * 操作图标宽度
     */
    @Dimension(unit = Dimension.PX)
    var operationIconWidth:Float = toolbarConfig.operationIconWidth

    /**
     * 操作图标高度
     */
    @Dimension(unit = Dimension.PX)
    var operationIconHeight:Float = toolbarConfig.operationIconHeight

    /**
     * 操作区域文字
     */
    var operationText: String? = toolbarConfig.operationText

    /**
     * 操作区域文字大小
     */
    @Dimension(unit = Dimension.PX)
    var operationTextSize: Float = toolbarConfig.operationTextSize

    /**
     * 操作区域文字颜色
     */
    @ColorInt
    var operationTextColor: Int = toolbarConfig.operationTextColor

    /**
     * 菜单文字大小
     */
    @Dimension(unit = Dimension.PX)
    var menuTextSize:Float = toolbarConfig.menuTextSize

    /**
     * 菜单文字颜色
     */
    @ColorInt
    var menuTextColor:Int = toolbarConfig.menuTextColor

    /**
     * 菜单文字位置
     */
    var menuTextLocation:Int = toolbarConfig.menuTextLocation

    /**
     * 文字距离图片
     */
    @Dimension(unit = Dimension.PX)
    var menuTextMarginIcon:Float = toolbarConfig.menuTextMarginIcon

}