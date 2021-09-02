package com.core.widget.toolbar

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import com.core.widget.R

/**
 * 普通ToolBar的布局
 * @author like
 * @date 8/30/21 11:18 AM
 */
class SimpleToolbar : BaseToolbar {

    private var title: String? = null

    @JvmOverloads
    constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int =0) : super(context, attributeSet, defStyleAttr) {
        title = if (!isInEditMode) (context as? Activity)?.title?.toString() else ""
        initAttr(context, attributeSet)
        initView()
    }

    private fun initAttr(context: Context, attributeSet: AttributeSet?) =
        with(context.obtainStyledAttributes(attributeSet, R.styleable.SimpleToolbar)) {

            for (i in 0 until indexCount) {
                when (val attr = getIndex(i)) {
                    R.styleable.SimpleToolbar_lk_toolbar_title -> {
                        title = getString(attr)
                    }
                    R.styleable.SimpleToolbar_lk_toolbar_title_text_color -> {
                        builder.titleTextColor = getColor(attr, builder.themeColor)
                    }
                    R.styleable.SimpleToolbar_lk_toolbar_title_text_size -> {
                        builder.titleTextSize = getDimension(attr, builder.titleTextSize)
                    }
                    R.styleable.SimpleToolbar_android_textStyle -> {
                        builder.titleTextStyle = getInt(attr, builder.titleTextStyle)
                    }
                }
            }
            recycle()
        }

    private fun initView(){
        binding.toolbarMiddleLayout.visibility = GONE
        binding.toolbarTitle.visibility = VISIBLE
        binding.toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.titleTextSize)
        binding.toolbarTitle.setTextColor(builder.titleTextColor ?: builder.themeColor)
        binding.toolbarTitle.text = title
        binding.toolbarTitle.typeface = Typeface.defaultFromStyle(builder.titleTextStyle)
    }

    /**
     * 自定义中间布局
     */
    override fun contentLayout(parent: FrameLayout) {

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
    fun setTitleTextSize(textSize: Float) {
        builder.titleTextSize = textSize
        binding.toolbarTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 设置标题颜色
     */
    fun setTitleTextColor(color: Int) {
        builder.titleTextColor = color
        binding.toolbarTitle.setTextColor(color)
    }

    /**
     * 设置标题是否样式
     */
    fun setTitleTextStyle(style: Int) {
        builder.titleTextStyle = style
        binding.toolbarTitle.typeface = Typeface.defaultFromStyle(style)
    }

}