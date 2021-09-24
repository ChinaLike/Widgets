package com.core.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
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

    /**
     * 菜单文字
     */
    private var simpleMenuText: String? = ""

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attributeSet, defStyleAttr) {
        orientation = VERTICAL
        initAttr(context, attributeSet)
        setPadding(0, getStatusBarHeight(), 0, 0)
        contentLayout(binding.toolbarMiddleLayout)

        initView()
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet?) =
        with(context.obtainStyledAttributes(attributeSet, R.styleable.BaseToolbar)) {
            for (i in 0 until indexCount) {
                when (val attr = getIndex(i)) {
                    R.styleable.BaseToolbar_lk_show_left_layout -> {
                        val showLeftLayout = getBoolean(attr, true)
                        binding.toolbarLeftLayout.visibility = if (showLeftLayout) VISIBLE else GONE
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
                        binding.toolbarRightLayout.visibility = if (showRightLayout) VISIBLE else GONE
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
                    R.styleable.BaseToolbar_lk_toolbar_menu_text -> {
                        simpleMenuText = getString(attr)
                    }

                    R.styleable.BaseToolbar_lk_toolbar_menu_text_size -> {
                        builder.menuTextSize = getDimension(attr, builder.menuTextSize)
                    }
                    R.styleable.BaseToolbar_lk_toolbar_menu_text_color -> {
                        builder.menuTextColor = getColor(attr, builder.menuTextColor)
                    }
                    R.styleable.BaseToolbar_lk_toolbar_menu_text_location -> {
                        builder.menuTextLocation = getInt(attr, builder.menuTextLocation)
                    }
                    R.styleable.BaseToolbar_lk_toolbar_menu_text_margin_icon -> {
                        builder.menuTextMarginIcon = getDimension(attr, builder.menuTextMarginIcon)
                    }
                }
            }
            recycle()
        }

    private fun initView() {
        binding.toolbarBackIcon.setImageDrawable(builder.backIcon)
        binding.toolbarBackIcon.setColorFilter(builder.backIconColor ?: builder.themeColor)

        binding.toolbarBackText.visibility = if (builder.showBackText) VISIBLE else GONE
        binding.toolbarBackText.text = builder.backText

        binding.toolbarBackText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.backTextSize)
        binding.toolbarBackText.setTextColor(builder.backTextColor ?: builder.themeColor)
        //设置间距
        binding.toolbarContentLayout.setPadding(builder.toolbarPaddingLeft.toInt(), 0, builder.toolbarPaddingRight.toInt(), 0)
        binding.toolbarBackText.setPadding(0, 0, builder.backTextMarginLeft.toInt(), 0)

        setContentMargin(builder.toolbarContentMarginLeft, builder.toolbarContentMarginRight)

        customMenu()
        //分割线
        binding.toolbarDivider.setBackgroundColor(builder.dividerColor)
        binding.toolbarDivider.visibility = if (builder.showDivider) VISIBLE else GONE

        binding.toolbarBackIcon.onDebouncedClick {
            if (onToolbarListener == null || onToolbarListener?.onCallback() == true) {
                (context as Activity).finish()
            }
        }
        binding.toolbarBackText.onDebouncedClick {
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
        val rightWidth = binding.toolbarRightLayout.width
        val leftWidth = binding.toolbarLeftLayout.width
        val middleWidth = resources.displayMetrics.widthPixels
        -2 * (rightWidth.coerceAtLeast(leftWidth))
        -builder.toolbarContentMarginLeft.toInt()
        -builder.toolbarContentMarginRight.toInt()
        -builder.toolbarPaddingRight
        -builder.toolbarPaddingLeft
        binding.toolbarTitle.layoutParams = ConstraintLayout.LayoutParams(middleWidth, LayoutParams.WRAP_CONTENT).apply {
            leftToLeft = ConstraintSet.PARENT_ID
            rightToRight = ConstraintSet.PARENT_ID
            topToTop = ConstraintSet.PARENT_ID
            bottomToBottom = ConstraintSet.PARENT_ID
        }
    }

    fun setMenuView(view: View) {
        binding.toolbarRightLayout.removeAllViews()
        binding.toolbarRightLayout.addView(view)
    }

    /**
     * 自定义Menu
     */
    private fun customMenu() {
        if (simpleMenuDrawable == null && TextUtils.isEmpty(simpleMenuText)) {
            return
        }
        val contentLayout = LinearLayout(context).apply {
            layoutParams = LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER
            dividerDrawable = shape(builder.menuTextMarginIcon)
            showDividers = SHOW_DIVIDER_MIDDLE
        }

        //图片
        val imageView = AppCompatImageView(context).apply {
            layoutParams = LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        }
        //文字
        val textView = AppCompatTextView(context).apply {
            layoutParams = LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            setTextSize(TypedValue.COMPLEX_UNIT_PX,builder.menuTextSize)
            setTextColor(builder.menuTextColor)
        }

        when (builder.menuTextLocation) {
            Location.TOP -> {
                contentLayout.orientation = VERTICAL
                simpleMenuText?.let {
                    textView.text = it
                    contentLayout.addView(textView)
                }
                simpleMenuDrawable?.let {
                    imageView.setImageResource(it)
                    contentLayout.addView(imageView)
                }
            }
            Location.LEFT -> {
                contentLayout.orientation = HORIZONTAL
                simpleMenuText?.let {
                    textView.text = it
                    contentLayout.addView(textView)
                }
                simpleMenuDrawable?.let {
                    imageView.setImageResource(it)
                    contentLayout.addView(imageView)
                }
            }
            Location.RIGHT -> {
                contentLayout.orientation = HORIZONTAL
                simpleMenuDrawable?.let {
                    imageView.setImageResource(it)
                    contentLayout.addView(imageView)
                }
                simpleMenuText?.let {
                    textView.text = it
                    contentLayout.addView(textView)
                }
            }
            else -> {
                contentLayout.orientation = VERTICAL
                simpleMenuDrawable?.let {
                    imageView.setImageResource(it)
                    contentLayout.addView(imageView)
                }
                simpleMenuText?.let {
                    textView.text = it
                    contentLayout.addView(textView)
                }
            }
        }
        binding.toolbarRightLayout.apply {
            removeAllViews()
            addView(contentLayout)
            onDebouncedClick {
                onToolbarListener?.onMenuClick(imageView, textView)
            }
        }
    }

    /**
     * 设置右侧图标，仅支持一个图标的按钮，如果是多个，添加自定义View
     */
    @JvmOverloads
    fun setMenuResource(@DrawableRes resId: Int? = null, text: String? = null) {
        this.simpleMenuDrawable = resId
        this.simpleMenuText = text
        customMenu()
    }

    /**
     * 显示左侧视图
     */
    fun showLeftLayout(show: Boolean) {
        binding.toolbarLeftLayout.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 显示右侧视图
     */
    fun showRightLayout(show: Boolean) {
        binding.toolbarRightLayout.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置返回图标
     */
    fun setBackIcon(backIcon: Drawable) {
        builder.backIcon = backIcon
        binding.toolbarBackIcon.setImageDrawable(backIcon)
    }

    /**
     * 设置返回图标颜色
     */
    fun setBackIconColor(@ColorInt backIconColor: Int) {
        builder.backIconColor = backIconColor
        binding.toolbarBackIcon.setColorFilter(backIconColor)
    }

    /**
     * 显示返回文本
     */
    fun showBackText(show: Boolean) {
        builder.showBackText = show
        binding.toolbarBackText.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置返回文本
     */
    fun setBackText(backText: String) {
        builder.backText = backText
        binding.toolbarBackText.text = backText
    }

    /**
     * 设置返回文本距离返回按钮的距离
     */
    fun setBackTextMarginLeft(backTextMarginLeft: Float) {
        builder.backTextMarginLeft = backTextMarginLeft
        binding.toolbarBackIcon.setPadding(0, 0, backTextMarginLeft.toInt(), 0)
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
        binding.toolbarBackText.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
        binding.toolbarBackText.setTextColor(textColor ?: builder.themeColor)
    }

    /**
     * 设置左内边距
     */
    fun setToolbarPaddingLeft(paddingLeft: Float) {
        builder.toolbarPaddingLeft = paddingLeft
        binding.toolbarContentLayout.setPadding(paddingLeft.toInt(), 0, builder.toolbarPaddingRight.toInt(), 0)
    }

    /**
     * 设置右内边距
     */
    fun setToolbarPaddingRight(paddingRight: Float) {
        builder.toolbarPaddingRight = paddingRight
        binding.toolbarContentLayout.setPadding(builder.toolbarPaddingLeft.toInt(), 0, paddingRight.toInt(), 0)
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
        if (binding.toolbarMiddleLayout.layoutParams is MarginLayoutParams) {
            (binding.toolbarMiddleLayout.layoutParams as MarginLayoutParams).leftMargin = marginLeft.toInt()
            (binding.toolbarMiddleLayout.layoutParams as MarginLayoutParams).rightMargin = marginRight.toInt()
        }
    }

    /**
     * 设置显示分割线
     */
    fun showDivider(show: Boolean) {
        builder.showDivider = show
        binding.toolbarDivider.visibility = if (show) VISIBLE else GONE
    }

    /**
     * 设置分割线颜色
     */
    fun setDividerColor(color: Int) {
        builder.dividerColor = color
        binding.toolbarDivider.setBackgroundColor(color)
    }

    /**
     * 自定义Shape
     */
    private fun shape(size: Float): GradientDrawable {
        return GradientDrawable().apply {
            this.setSize(size.toInt(), size.toInt())
            this.setColor(Color.TRANSPARENT)
        }
    }

}