package com.core.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.core.ex.onDebouncedClick
import com.core.widget.R
import com.core.widget.databinding.LkToolbarBaseBinding

/**
 *
 * @author like
 * @date 8/30/21 11:18 AM
 */
abstract class BaseToolbar : LinearLayout {

    protected val binding = LkToolbarBaseBinding.inflate(LayoutInflater.from(context), this)

    /**
     * 全局配置参数
     */
    internal val builder: Builder = Builder(context)

    /**
     * 搜索监听器
     */
    var onToolbarListener: OnToolbarListener? = null


    @DrawableRes
    private var simpleMenuDrawable: Int? = null

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attributeSet, defStyleAttr) {
        orientation = VERTICAL
        initAttr(context, attributeSet)
        setPadding(0, getStatusBarHeight(), 0, 0)
        contentLayout(binding.middleLayout)

        initView()
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet?) =
        with(context.obtainStyledAttributes(attributeSet, R.styleable.BaseToolbar)) {
            for (i in 0 until indexCount) {
                when (val attr = getIndex(i)) {
                    R.styleable.BaseToolbar_lk_show_left_layout -> {
                        val showLeftLayout = getBoolean(attr, true)
                        binding.leftLayout.visibility = if (showLeftLayout) VISIBLE else GONE
                    }
                    R.styleable.BaseToolbar_lk_back_icon -> {
                        builder.backIcon = getDrawable(attr)
                    }
                    R.styleable.BaseToolbar_lk_back_icon_color -> {
                        builder.backIconColor = getColor(attr, builder.themeColor)
                    }
                    R.styleable.BaseToolbar_lk_show_back_text -> {
                        builder.showBackText = getBoolean(attr, false)
                    }
                    R.styleable.BaseToolbar_lk_back_text -> {
                        builder.backText = getString(attr) ?: builder.backText
                    }
                    R.styleable.BaseToolbar_lk_back_text_margin_left -> {
                        builder.backTextMarginLeft = getDimension(attr, builder.backTextMarginLeft)
                    }
                    R.styleable.BaseToolbar_lk_back_text_size -> {
                        builder.backTextSize = getDimension(attr, builder.backTextSize)
                    }
                    R.styleable.BaseToolbar_lk_back_text_color -> {
                        builder.backTextColor = getColor(attr, builder.themeColor)
                    }
                    R.styleable.BaseToolbar_lk_show_right_layout -> {
                        val showRightLayout = getBoolean(attr, false)
                        binding.rightLayout.visibility = if (showRightLayout) VISIBLE else GONE
                    }
                    R.styleable.BaseToolbar_lk_padding_left -> {
                        builder.toolbarPaddingLeft = getDimension(attr, builder.toolbarPaddingLeft)
                    }
                    R.styleable.BaseToolbar_lk_padding_right -> {
                        builder.toolbarPaddingRight = getDimension(attr, builder.toolbarPaddingRight)
                    }
                    R.styleable.BaseToolbar_lk_content_margin_left -> {
                        builder.toolbarContentMarginLeft = getDimension(attr, builder.toolbarContentMarginLeft)
                    }
                    R.styleable.BaseToolbar_lk_content_margin_right -> {
                        builder.toolbarContentMarginRight = getDimension(attr, builder.toolbarContentMarginRight)
                    }
                    R.styleable.BaseToolbar_lk_show_divider -> {
                        builder.showDivider = getBoolean(attr, builder.showDivider)
                    }
                    R.styleable.BaseToolbar_lk_divider_color -> {
                        builder.dividerColor = getColor(attr, builder.dividerColor)
                    }
                    R.styleable.BaseToolbar_lk_toolbar_menu_icon -> {
                        simpleMenuDrawable = getResourceId(attr, 0)
                    }
                }
            }
            recycle()
        }

    private fun initView() {
        binding.backIcon.setImageDrawable(builder.backIcon)
        binding.backIcon.setColorFilter(builder.backIconColor ?: builder.themeColor)

        binding.backText.visibility = if (builder.showBackText) VISIBLE else GONE
        binding.backText.text = builder.backText

        binding.backText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.backTextSize)
        binding.backText.setTextColor(builder.backTextColor ?: builder.themeColor)
        //设置间距
        binding.contentLayout.setPadding(builder.toolbarPaddingLeft.toInt(), 0, builder.toolbarPaddingRight.toInt(), 0)
        binding.backIcon.setPadding(0, 0, builder.backTextMarginLeft.toInt(), 0)

        setContentMargin(builder.toolbarContentMarginLeft, builder.toolbarContentMarginRight)

        simpleMenuDrawable?.let { setMenuResource(it) }
        //分割线
        binding.divider.setBackgroundColor(builder.dividerColor)
        binding.divider.visibility = if (builder.showDivider) VISIBLE else GONE

        binding.backIcon.onDebouncedClick {
            if (onToolbarListener == null || onToolbarListener?.onCallback() == true) {
                (context as Activity).finish()
            }
        }
        binding.backText.onDebouncedClick {
            onToolbarListener?.onClose()
        }
    }

    /**
     * 获取状态栏高度
     */
    private fun getStatusBarHeight(): Int {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return 0
        }
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    /**
     * 自定义中间布局
     */
    abstract fun contentLayout(parent: FrameLayout)

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val rightWidth = binding.rightLayout.width
        val leftWidth = binding.leftLayout.width
        val middleWidth = resources.displayMetrics.widthPixels
        -2 * (rightWidth.coerceAtLeast(leftWidth))
        -builder.toolbarContentMarginLeft.toInt()
        -builder.toolbarContentMarginRight.toInt()
        -builder.toolbarPaddingRight
        -builder.toolbarPaddingLeft
        binding.title.layoutParams = ConstraintLayout.LayoutParams(middleWidth, LayoutParams.WRAP_CONTENT).apply {
            leftToLeft = ConstraintSet.PARENT_ID
            rightToRight = ConstraintSet.PARENT_ID
            topToTop = ConstraintSet.PARENT_ID
            bottomToBottom = ConstraintSet.PARENT_ID
        }
    }

    fun setMenuView(view: View) {
        binding.rightLayout.removeAllViews()
        binding.rightLayout.addView(view)
    }

    /**
     * 设置右侧图标，仅支持一个图标的按钮，如果是多个，添加自定义View
     */
    fun setMenuResource(@DrawableRes resId: Int) {
        val imageView = AppCompatImageView(context).apply {
            layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            setImageResource(resId)
        }
        binding.rightLayout.apply {
            removeAllViews()
            addView(imageView)
        }
    }

    /**
     * 显示左侧视图
     */
    fun showLeftLayout(show: Boolean) {
        binding.leftLayout.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 显示右侧视图
     */
    fun showRightLayout(show: Boolean) {
        binding.rightLayout.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置返回图标
     */
    fun setBackIcon(backIcon: Drawable) {
        builder.backIcon = backIcon
        binding.backIcon.setImageDrawable(backIcon)
    }

    /**
     * 设置返回图标颜色
     */
    fun setBackIconColor(@ColorInt backIconColor: Int) {
        builder.backIconColor = backIconColor
        binding.backIcon.setColorFilter(backIconColor)
    }

    /**
     * 显示返回文本
     */
    fun showBackText(show: Boolean) {
        builder.showBackText = show
        binding.backText.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置返回文本
     */
    fun setBackText(backText: String) {
        builder.backText = backText
        binding.backText.text = backText
    }

    /**
     * 设置返回文本距离返回按钮的距离
     */
    fun setBackTextMarginLeft(backTextMarginLeft: Float) {
        builder.backTextMarginLeft = backTextMarginLeft
        binding.backIcon.setPadding(0, 0, backTextMarginLeft.toInt(), 0)
    }

    /**
     * 设置返回文本的样式
     * @param [textColor] 颜色
     * @param [textSize] 大小
     */
    @JvmOverloads
    fun setBackTextStyle(textSize: Float = builder.backTextSize, textColor: Int? = builder.backTextColor) {
        builder.backTextSize = textSize
        builder.backTextColor = textColor
        binding.backText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        binding.backText.setTextColor(textColor ?: builder.themeColor)
    }

    /**
     * 设置左内边距
     */
    fun setToolbarPaddingLeft(paddingLeft: Float) {
        builder.toolbarPaddingLeft = paddingLeft
        binding.contentLayout.setPadding(paddingLeft.toInt(), 0, builder.toolbarPaddingRight.toInt(), 0)
    }

    /**
     * 设置右内边距
     */
    fun setToolbarPaddingRight(paddingRight: Float) {
        builder.toolbarPaddingRight = paddingRight
        binding.contentLayout.setPadding(builder.toolbarPaddingLeft.toInt(), 0, paddingRight.toInt(), 0)
    }

    /**
     * 设置左外边距
     */
    fun setContentMarginLeft(marginLeft: Float) {
        builder.toolbarContentMarginLeft = marginLeft
        setContentMargin(marginLeft, builder.toolbarContentMarginRight)
    }

    /**
     * 设置右外边距
     */
    fun setContentMarginRight(marginRight: Float) {
        builder.toolbarContentMarginRight = marginRight
        setContentMargin(builder.toolbarContentMarginLeft, marginRight)
    }

    /**
     * 设置内容外边距
     */
    fun setContentMarginHorizontal(marginHorizontal: Float) {
        builder.toolbarContentMarginLeft = marginHorizontal
        builder.toolbarContentMarginRight = marginHorizontal
        setContentMargin(marginHorizontal, marginHorizontal)
    }

    /**
     * 设置内容外边距
     */
    private fun setContentMargin(marginLeft: Float, marginRight: Float) {
        if (binding.middleLayout.layoutParams is MarginLayoutParams) {
            (binding.middleLayout.layoutParams as MarginLayoutParams).leftMargin = marginLeft.toInt()
            (binding.middleLayout.layoutParams as MarginLayoutParams).rightMargin = marginRight.toInt()
        }
    }

    /**
     * 设置显示分割线
     */
    fun showDivider(show: Boolean) {
        builder.showDivider = show
        binding.divider.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置分割线颜色
     */
    fun setDividerColor(color: Int) {
        builder.dividerColor = color
        binding.divider.setBackgroundColor(color)
    }

}