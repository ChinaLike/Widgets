package com.core.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.core.ex.onDebouncedClick
import com.core.widget.R
import com.core.widget.databinding.LkToolbarSearchBinding
import com.core.widget.edit.OnTextChangeListener

/**
 * 带搜索的ToolBar的布局
 * @author like
 * @date 8/30/21 2:30 PM
 *
 * TODO 此文件有问题，只是暂时满足需求，等新需求做完了，再来优化
 */
class SearchToolbar : BaseToolbar, OnTextChangeListener, TextView.OnEditorActionListener {

    private var childBinding: LkToolbarSearchBinding? = null

    /**
     * 显示操作区域
     */
    private var showOperation: Boolean = false

    /**
     * 默认显示搜索框
     */
    private var defaultShowSearch: Boolean = true

    /**
     * 搜索框是否可用
     */
    private var searchEnable: Boolean = true


    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attributeSet, defStyleAttr) {
        toolbarTitle = if (!isInEditMode) (context as? Activity)?.title?.toString() else ""

        initAttr(context, attributeSet)
        initView()
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet?) =
        with(context.obtainStyledAttributes(attributeSet, R.styleable.SearchToolbar)) {
            for (i in 0 until indexCount) {
                when (val attr = getIndex(i)) {
                    R.styleable.SearchToolbar_lk_toolbar_title -> {
                        toolbarTitle = getString(attr)
                    }
                    R.styleable.SearchToolbar_lk_toolbar_title_text_color -> {
                        builder.titleTextColor = getColor(attr, builder.themeColor)
                    }
                    R.styleable.SearchToolbar_lk_toolbar_title_text_size -> {
                        builder.titleTextSize = getDimension(attr, builder.titleTextSize)
                    }
                    R.styleable.SearchToolbar_lk_search_height -> {
                        builder.searchHeight = getDimension(attr, builder.searchHeight)
                    }
                    R.styleable.SearchToolbar_lk_search_radius -> {
                        builder.searchRadius = getDimension(attr, 999999F)
                    }
                    R.styleable.SearchToolbar_lk_search_background -> {
                        builder.searchBackground = getDrawable(attr)
                    }
                    R.styleable.SearchToolbar_lk_search_stroke_width -> {
                        builder.searchStrokeWidth = getDimension(attr, builder.searchStrokeWidth)
                    }
                    R.styleable.SearchToolbar_lk_search_stroke_color -> {
                        builder.searchStrokeColor = getColor(attr, builder.searchStrokeColor)
                    }
                    R.styleable.SearchToolbar_lk_search_padding_left -> {
                        builder.searchPaddingLeft = getDimension(attr, builder.searchPaddingLeft)
                    }
                    R.styleable.SearchToolbar_lk_search_padding_right -> {
                        builder.searchPaddingRight = getDimension(attr, builder.searchPaddingRight)
                    }
                    R.styleable.SearchToolbar_lk_search_margin_right -> {
                        builder.searchMarginRight = getDimension(attr, builder.searchMarginRight)
                    }
                    R.styleable.SearchToolbar_lk_default_show_search -> {
                        defaultShowSearch = getBoolean(attr, defaultShowSearch)
                    }
                    R.styleable.SearchToolbar_lk_search_before_icon -> {
                        builder.searchBeforeIcon = getDrawable(attr)
                    }
                    R.styleable.SearchToolbar_lk_search_before_icon_width -> {
                        builder.searchBeforeIconWidth = getDimension(attr, builder.searchBeforeIconWidth)
                    }
                    R.styleable.SearchToolbar_lk_search_before_icon_height -> {
                        builder.searchBeforeIconHeight = getDimension(attr, builder.searchBeforeIconHeight)
                    }
                    R.styleable.SearchToolbar_lk_search_before_icon_color -> {
                        builder.searchBeforeIconColor = getColor(attr, builder.themeColor)
                    }
                    R.styleable.SearchToolbar_lk_search_edit_margin_left -> {
                        builder.searchEditMarginLeft = getDimension(attr, builder.searchEditMarginLeft)
                    }
                    R.styleable.SearchToolbar_lk_search_hint_text -> {
                        builder.searchHintText = getString(attr)
                    }
                    R.styleable.SearchToolbar_lk_search_enable -> {
                        searchEnable = getBoolean(attr, searchEnable)
                    }
                    R.styleable.SearchToolbar_lk_search_hint_text_size -> {
                        builder.searchHintTextSize = getDimension(attr, builder.searchHintTextSize)
                    }
                    R.styleable.SearchToolbar_lk_search_hint_text_color -> {
                        builder.searchHintTextColor = getColor(attr, builder.searchHintTextColor)
                    }
                    R.styleable.SearchToolbar_lk_search_text -> {
                        builder.searchText = getString(attr)
                    }
                    R.styleable.SearchToolbar_lk_search_text_size -> {
                        builder.searchTextSize = getDimension(attr, builder.searchTextSize)
                    }
                    R.styleable.SearchToolbar_lk_search_text_color -> {
                        builder.searchTextColor = getColor(attr, builder.searchTextColor)
                    }
                    R.styleable.SearchToolbar_lk_show_operation -> {
                        showOperation = getBoolean(attr, showOperation)
                    }
                    R.styleable.SearchToolbar_lk_operation_icon -> {
                        builder.operationIcon = getDrawable(attr)
                    }
                    R.styleable.SearchToolbar_lk_operation_icon_width -> {
                        builder.operationIconWidth = getDimension(attr, builder.operationIconWidth)
                    }
                    R.styleable.SearchToolbar_lk_operation_icon_height -> {
                        builder.operationIconHeight = getDimension(attr, builder.operationIconHeight)
                    }
                    R.styleable.SearchToolbar_lk_operation_text -> {
                        builder.operationText = getString(attr)
                    }
                    R.styleable.SearchToolbar_lk_operation_text_size -> {
                        builder.operationTextSize = getDimension(attr, builder.operationTextSize)
                    }
                    R.styleable.SearchToolbar_lk_operation_text_color -> {
                        builder.operationTextColor = getColor(attr, builder.operationTextColor)
                    }
                    R.styleable.SearchToolbar_android_textStyle -> {
                        builder.titleTextStyle = getInt(attr, builder.titleTextStyle)
                    }
                    R.styleable.SearchToolbar_lk_show_search_before_icon -> {
                        builder.showSearchBeforeIcon = getBoolean(attr, builder.showSearchBeforeIcon)
                    }
                }
            }
            recycle()
        }


    private fun initView() {
        childBinding?.apply {
            setSearchLayoutParams()
            setSearchBackground(builder.searchBackground)
            setSearchPadding()
            defaultShowSearch(defaultShowSearch)

            //输入框前的图标
            toolbarSearchIcon.setImageDrawable(builder.searchBeforeIcon)
            builder.searchBeforeIconColor?.let { toolbarSearchIcon.setColorFilter(it) }
            toolbarSearchIcon.visibility = if (builder.showSearchBeforeIcon) View.VISIBLE else View.GONE

            //搜索输入框距离图标距离
            setEditMarginLeft(builder.searchEditMarginLeft)
            //设置输入框提示语
            setSearchHintTextSize(builder.searchHintTextSize)
            toolbarClearEditText.setHintTextColor(builder.searchHintTextColor)
            //设置输入框文本
            toolbarClearEditText.setText(builder.searchText)
            toolbarClearEditText.setTextColor(builder.searchTextColor)
            toolbarClearEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.searchTextSize)
            //  设置输入框是否可用
            setSearchEnable(searchEnable)
            //设置操作图标
            toolbarOperationIcon.setImageDrawable(builder.operationIcon)
            toolbarOperationText.text = builder.operationText
            toolbarOperationText.setTextColor(builder.operationTextColor)
            toolbarOperationText.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.operationTextSize)
            toolbarOperationLayout.visibility = if (showOperation) VISIBLE else GONE
            //标题
            binding.toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.titleTextSize)
            binding.toolbarTitle.setTextColor(builder.titleTextColor ?: builder.themeColor)
            binding.toolbarTitle.text = toolbarTitle

            toolbarOperationIcon.onDebouncedClick {
                toolbarInputLayout.visibility = VISIBLE
                binding.toolbarTitle.visibility = GONE
                toolbarOperationText.visibility = VISIBLE
                toolbarOperationIcon.visibility = GONE
                showKeyboard()
            }
            toolbarOperationText.onDebouncedClick {
                hideKeyboard()
                childBinding?.toolbarClearEditText?.setText("")
                val change = onToolbarListener?.onSearchCancel()
                if (onToolbarListener == null || change == true) {
                    toolbarInputLayout.visibility = GONE
                    binding.toolbarTitle.visibility = VISIBLE
                    toolbarOperationText.visibility = GONE
                    toolbarOperationIcon.visibility = VISIBLE
                }
            }

            toolbarClearEditText.setOnTextChangeListener(this@SearchToolbar)
            toolbarClearEditText.setOnEditorActionListener(this@SearchToolbar)

            setSearchBeforeIconSize(builder.searchBeforeIconWidth, builder.searchBeforeIconHeight)
            setOperationIconSize(builder.operationIconWidth, builder.operationIconHeight)

            //设置布局
            if (!searchEnable) {
                childBinding?.toolbarClearEditText?.onDebouncedClick {
                    onToolbarListener?.onMiddleLayoutClick(toolbarClearEditText.text.toString())
                }
            }
        }
    }

    /**
     * 自定义中间布局
     */
    override fun contentLayout(parent: FrameLayout) {
        childBinding = LkToolbarSearchBinding.inflate(LayoutInflater.from(context), parent, true)
    }

    /**
     * 设置搜索框背景
     */
    fun setSearchBackground(drawable: Drawable?) {
        builder.searchBackground = drawable
        if (drawable is GradientDrawable) {
            setDrawable(childBinding?.toolbarInputLayout, drawable.apply {
                setStroke(builder.searchStrokeWidth.toInt(), builder.searchStrokeColor)
                cornerRadius = builder.searchRadius ?: 999999F
            })
        } else if (drawable is ColorDrawable) {
            setDrawable(childBinding?.toolbarInputLayout, GradientDrawable().apply {
                setColor(drawable.color)
                setStroke(builder.searchStrokeWidth.toInt(), builder.searchStrokeColor)
                cornerRadius = builder.searchRadius ?: 999999F
            })
        } else {
            setDrawable(childBinding?.toolbarInputLayout, GradientDrawable().apply {
                setColor(Color.TRANSPARENT)
                setStroke(builder.searchStrokeWidth.toInt(), builder.searchStrokeColor)
                cornerRadius = builder.searchRadius ?: 999999F
            })
        }
    }

    /**
     * 设置输入框提示语大小
     */
    fun setSearchHintTextSize(@Dimension(unit = Dimension.PX) hintSize: Float) {
        builder.searchHintTextSize = hintSize
        val str = SpannableString(builder.searchHintText)
        val absoluteSizeSpan = AbsoluteSizeSpan(builder.searchHintTextSize.toInt(), false)
        str.setSpan(absoluteSizeSpan, 0, str.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        childBinding?.toolbarClearEditText?.hint = SpannedString(str)

    }

    private fun setDrawable(view: View?, drawable: Drawable) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            view?.background = drawable
        } else {
            view?.setBackgroundDrawable(drawable)
        }
    }

    /**
     * 隐藏键盘
     */
    fun hideKeyboard() {
        val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(windowToken, 0)
    }

    /**
     * 显示键盘
     */
    fun showKeyboard() {
        if (searchEnable) {
            childBinding?.toolbarClearEditText?.isFocusable = true
            childBinding?.toolbarClearEditText?.isFocusableInTouchMode = true
            childBinding?.toolbarClearEditText?.requestFocus()
            childBinding?.toolbarClearEditText?.setSelection(childBinding?.toolbarClearEditText?.text?.toString()?.trim()?.length ?: 0)
            val manager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.showSoftInput(childBinding?.toolbarClearEditText, 0)
        }
    }

    override fun onTextChanged(text: CharSequence?, start: Int, lengthBefore: Int, lengthAfter: Int) {
        onToolbarListener?.onSearchTextChanged(text?.toString())
    }

    /**
     * Called when an action is being performed.
     *
     * @param v The view that was clicked.
     * @param actionId Identifier of the action.  This will be either the
     * identifier you supplied, or [ EditorInfo.IME_NULL][EditorInfo.IME_NULL] if being called due to the enter key
     * being pressed.
     * @param event If triggered by an enter key, this is the event;
     * otherwise, this is null.
     * @return Return true if you have consumed the action, else false.
     */
    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            //隐藏键盘
            hideKeyboard()
            onToolbarListener?.search(childBinding?.toolbarClearEditText?.text?.toString())
            return true
        }
        return false
    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        binding.toolbarTitle.text = title
    }

    /**
     * 设置标题大小
     */
    fun setTitleTextSize(@Dimension(unit = Dimension.PX) textSize: Float) {
        builder.titleTextSize = textSize
        binding.toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 设置标题颜色
     * @param [color] 标题颜色
     */
    fun setTitleTextColor(@ColorInt color: Int) {
        builder.titleTextColor = color
        binding.toolbarTitle.setTextColor(color)
    }

    /**
     * 设置标题是否样式
     * @param [style] 样式 [Typeface]
     */
    fun setTitleTextStyle(style: Int) {
        builder.titleTextStyle = style
        binding.toolbarTitle.typeface = Typeface.defaultFromStyle(style)
    }


    /**
     * 设置搜索框的高度
     * @param [height] 搜索框高度
     */
    fun setSearchHeight(@Dimension(unit = Dimension.PX) height: Float) {
        builder.searchHeight = height
        setSearchLayoutParams()
    }

    /**
     * 设置搜索框的圆角
     * @param [radius] 搜索框圆角
     */
    fun setSearchRadius(@Dimension(unit = Dimension.PX) radius: Float) {
        builder.searchRadius = radius
        setSearchBackground(builder.searchBackground)
    }

    /**
     * 设置搜索框边框
     * @param [width] 边框宽度
     * @param [color] 边框颜色
     */
    @JvmOverloads
    fun setSearchStroke(@Dimension(unit = Dimension.PX) width: Float = builder.searchStrokeWidth, @ColorInt color: Int = builder.searchStrokeColor) {
        builder.searchStrokeWidth = width
        builder.searchStrokeColor = color
        setSearchBackground(builder.searchBackground)
    }

    /**
     * 设置搜索框左边内边距
     * @param [paddingLeft] 左内边距
     */
    fun setSearchPaddingLeft(@Dimension(unit = Dimension.PX) paddingLeft: Float) {
        builder.searchPaddingLeft = paddingLeft
        setSearchPadding()
    }

    /**
     * 设置搜索框右边内边距
     * @param [paddingRight] 右内边距
     */
    fun setSearchPaddingRight(@Dimension(unit = Dimension.PX) paddingRight: Float) {
        builder.searchPaddingRight = paddingRight
        setSearchPadding()
    }

    /**
     * 设置搜索框右边外边距
     * @param [marginRight] 右外边距
     */
    fun setSearchMarginRight(@Dimension(unit = Dimension.PX) marginRight: Float) {
        builder.searchMarginRight = marginRight
        setSearchLayoutParams()
    }

    /**
     * 设置搜索框左右边距
     * @param [paddingHorizontal] 左右边距
     */
    fun setSearchPaddingHorizontal(@Dimension(unit = Dimension.PX) paddingHorizontal: Float) {
        builder.searchPaddingLeft = paddingHorizontal
        builder.searchPaddingRight = paddingHorizontal
        setSearchPadding()
    }


    private fun setSearchLayoutParams() {
        childBinding?.apply {
            toolbarInputLayout?.layoutParams = ConstraintLayout.LayoutParams(0, builder.searchHeight.toInt()).apply {
                bottomToBottom = ConstraintSet.PARENT_ID
                leftToLeft = ConstraintSet.PARENT_ID
                topToTop = ConstraintSet.PARENT_ID
                rightToLeft = toolbarOperationLayout.id
                rightMargin = if (showOperation) builder.searchMarginRight.toInt() else 0
            }
        }
    }

    /**
     * 设置内边距
     */
    private fun setSearchPadding() {
        childBinding?.toolbarInputLayout?.setPadding(builder.searchPaddingLeft.toInt(), 0, builder.searchPaddingRight.toInt(), 0)
    }

    /**
     * 默认显示标题还是搜索框
     * @param [show] true 显示搜索框 false 显示标题
     */
    fun defaultShowSearch(show: Boolean) {
        defaultShowSearch = show
        childBinding?.apply {
            binding.toolbarTitle.visibility = if (defaultShowSearch) GONE else VISIBLE
            toolbarInputLayout.visibility = if (defaultShowSearch) VISIBLE else GONE
            if (defaultShowSearch) {
                toolbarOperationText.visibility = VISIBLE
                toolbarOperationIcon.visibility = GONE
            } else {
                toolbarOperationText.visibility = GONE
                toolbarOperationIcon.visibility = VISIBLE
            }
        }

    }

    /**
     * 搜索框内左边图标
     * @param [drawable] 图标
     */
    fun setSearchBeforeIcon(drawable: Drawable) {
        builder.searchBeforeIcon = drawable
        childBinding?.toolbarSearchIcon?.setImageDrawable(drawable)
    }

    /**
     * 设置搜索框前面图标大小
     * @return [width] 宽度
     * @return [height] 高度
     */
    fun setSearchBeforeIconSize(@Dimension(unit = Dimension.PX) width: Float, @Dimension(unit = Dimension.PX) height: Float) {
        builder.searchBeforeIconWidth = width
        builder.searchBeforeIconHeight = height
        childBinding?.toolbarSearchIcon?.layoutParams?.width = width.toInt()
        childBinding?.toolbarSearchIcon?.layoutParams?.height = height.toInt()
    }

    /**
     * 搜索框内左边图标的颜色
     * @param [color] 图标颜色
     */
    fun setSearchBeforeIconColor(@ColorInt color: Int) {
        builder.searchBeforeIconColor = color
        childBinding?.toolbarSearchIcon?.setColorFilter(color)
    }

    /**
     * 搜索框距离左边距离
     * @param [marginLeft] 文本颜色
     */
    fun setEditMarginLeft(@Dimension(unit = Dimension.PX) marginLeft: Float) {
        builder.searchEditMarginLeft = marginLeft
        val layoutParams = childBinding?.toolbarClearEditText?.layoutParams
        if (layoutParams is MarginLayoutParams) {
            layoutParams.leftMargin = builder.searchEditMarginLeft.toInt()
        }
    }

    /**
     * 搜索占位字符串
     * @param [hint] 搜索占位字符串
     */
    fun setSearchHintText(hint: String?) {
        builder.searchHintText = hint
        setSearchHintTextSize(builder.searchHintTextSize)
    }

    /**
     * 搜索文本是否可以点击搜索
     * @param [enable] true 是
     */
    fun setSearchEnable(enable: Boolean) {
        searchEnable = enable
        childBinding?.toolbarClearEditText?.isCursorVisible = enable
        childBinding?.toolbarClearEditText?.isFocusable = enable
        childBinding?.toolbarClearEditText?.setTextIsSelectable(enable)
    }

    /**
     * 搜索文本提示语颜色
     * @param [hintTextColor] 提示语颜色
     */
    fun setSearchHintTextColor(@ColorInt hintTextColor: Int) {
        builder.searchHintTextColor = hintTextColor
        childBinding?.toolbarClearEditText?.setHintTextColor(hintTextColor)

    }

    /**
     * 搜索文本
     * @param [searchText] 文本
     */
    fun setSearchText(searchText: String?) {
        builder.searchText = searchText
        childBinding?.toolbarClearEditText?.setText(searchText)
    }

    /**
     * 搜索文本大小
     * @param [textSize] 文本大小
     */
    fun setSearchTextSize(@Dimension(unit = Dimension.PX) textSize: Float) {
        builder.searchTextSize = textSize
        childBinding?.toolbarClearEditText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 搜索文本颜色
     * @param [textColor] 文本颜色
     */
    fun setSearchTextColor(@ColorInt textColor: Int) {
        builder.searchTextColor = textColor
        childBinding?.toolbarClearEditText?.setTextColor(textColor)
    }

    /**
     * 是否显示操作区域
     * @param [show] true 显示
     */
    fun showOperation(show: Boolean) {
        showOperation = show
        childBinding?.toolbarOperationLayout?.visibility = if (showOperation) VISIBLE else GONE
    }

    /**
     * 设置操作图标
     * @param [drawable] 图标
     */
    fun setOperationIcon(drawable: Drawable) {
        builder.operationIcon = drawable
        childBinding?.toolbarOperationIcon?.setImageDrawable(drawable)
    }

    /**
     * 设置搜索框前面图标大小
     * @return [width] 宽度
     * @return [height] 高度
     */
    fun setOperationIconSize(@Dimension(unit = Dimension.PX) width: Float, @Dimension(unit = Dimension.PX) height: Float) {
        builder.operationIconWidth = width
        builder.operationIconHeight = height
        childBinding?.toolbarOperationIcon?.layoutParams?.width = width.toInt()
        childBinding?.toolbarOperationIcon?.layoutParams?.height = height.toInt()
    }

    /**
     * 设置操作文本
     * @param [text] 文本
     */
    fun setOperationText(text: String) {
        builder.operationText = text
        childBinding?.toolbarOperationText?.text = text
    }

    /**
     * 设置操作文本大小
     * @param [textSize] 文本大小
     */
    fun setOperationTextSize(@Dimension(unit = Dimension.PX) textSize: Float) {
        builder.operationTextSize = textSize
        childBinding?.toolbarOperationText?.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 设置操作文本颜色
     * @param [textColor] 文本颜色
     */
    fun setOperationTextColor(@ColorInt textColor: Int) {
        builder.operationTextColor = textColor
        childBinding?.toolbarOperationText?.setTextColor(textColor)
    }

}