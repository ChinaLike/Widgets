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
        binding.middleLayout.visibility = GONE
        binding.title.visibility = VISIBLE
        title = if (!isInEditMode) (context as? Activity)?.title?.toString() else ""
        initAttr(context, attributeSet)

        binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, builder.titleTextSize)
        binding.title.setTextColor(builder.titleTextColor ?: builder.themeColor)
        binding.title.text = title
        binding.title.typeface = Typeface.defaultFromStyle(builder.titleTextStyle)
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

    /**
     * 自定义中间布局
     */
    override fun contentLayout(parent: FrameLayout) {

    }

    /**
     * 设置标题
     */
    fun setTitle(title: String?) {
        binding.title.text = title
    }

    /**
     * 设置标题大小
     */
    fun setTitleTextSize(textSize: Float) {
        builder.titleTextSize = textSize
        binding.title.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
    }

    /**
     * 设置标题颜色
     */
    fun setTitleTextColor(color: Int) {
        builder.titleTextColor = color
        binding.title.setTextColor(color)
    }

    /**
     * 设置标题是否样式
     */
    fun setTitleTextStyle(style: Int) {
        builder.titleTextStyle = style
        binding.title.typeface = Typeface.defaultFromStyle(style)
    }

}