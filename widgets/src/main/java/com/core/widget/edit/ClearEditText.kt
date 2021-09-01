package com.core.widget.edit

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.core.util.DensityUtil
import com.core.widget.R
import com.tsy.widget.edit.OnClearListener
import com.tsy.widget.edit.OnTextChangeListener

/**
 * 带清除按钮的输入框
 * @author like
 * @date 8/30/21 11:02 AM
 */
class ClearEditText : AppCompatEditText, View.OnFocusChangeListener, TextWatcher {

    /**
     * 默认Icon尺寸（dp）
     */
    private val defaultIconSize = 15F


    /**
     * 清除图标
     */
    private var clearIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.lk_icon_edit_text_clear)

    /**
     * 必须输入提醒
     */
    private var hintIcon: Drawable? = ContextCompat.getDrawable(context, R.drawable.lk_icon_edit_text_hint)

    /**
     * 输入框是否不可用
     */
    private var disabled: Boolean = false

    /**
     * 不可用时文本颜色
     */
    @ColorInt
    var disabledTextColor: Int? = null

    /**
     * 图标与输入框的距离
     */
    var iconMarginLeft: Float = DensityUtil.dp2px(context, 5F).toFloat()

    /**
     * 是否显示清除图标
     */
    var showClearIcon: Boolean = true

    /**
     * 是否显示提醒图标
     */
    var showHintIcon: Boolean = true

    /**
     * 是否必须输入
     */
    var require: Boolean = false

    /**
     * 清除图标大小
     */
    var clearIconSize: Int = DensityUtil.dp2px(context, defaultIconSize)

    /**
     * 清除图标的宽
     */
    var clearIconWidth: Int = clearIconSize

    /**
     * 清除图标的搞
     */
    var clearIconHeight: Int = clearIconSize

    /**
     * 提示图标大小
     */
    var hintIconSize: Int = DensityUtil.dp2px(context, defaultIconSize)

    /**
     * 提示图标的宽
     */
    var hintIconWidth: Int = hintIconSize

    /**
     * 提示图标的搞
     */
    var hintIconHeight: Int = hintIconSize

    private var onTextChangeListener: OnTextChangeListener? = null

    private var onClearListener: OnClearListener? = null

    /**
     * 清除图标是否显示
     */
    private var isClearShow: Boolean = false

    /**
     * 是否获取输入框焦点
     */
    private var hasFocus: Boolean = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, android.R.attr.editTextStyle)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)
        for (i in 0 until typedArray.indexCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.ClearEditText_cetClearIcon -> {
                    clearIcon = typedArray.getDrawable(attr)
                }
                R.styleable.ClearEditText_cetHintIcon -> {
                    hintIcon = typedArray.getDrawable(attr)
                }
                R.styleable.ClearEditText_cetDisabled -> {
                    disabled = typedArray.getBoolean(attr, disabled)

                }
                R.styleable.ClearEditText_cetDisabledColor -> {
                    disabledTextColor = typedArray.getColor(attr, currentTextColor)
                }
                R.styleable.ClearEditText_cetIconMarginLeft -> {
                    iconMarginLeft = typedArray.getDimension(attr, iconMarginLeft)
                }
                R.styleable.ClearEditText_cetShowClearIcon -> {
                    showClearIcon = typedArray.getBoolean(attr, showClearIcon)
                }
                R.styleable.ClearEditText_cetShowHintIcon -> {
                    showHintIcon = typedArray.getBoolean(attr, showHintIcon)
                }
                R.styleable.ClearEditText_cetRequire -> {
                    require = typedArray.getBoolean(attr, require)
                }
                R.styleable.ClearEditText_cetClearIconSize -> {
                    clearIconSize = typedArray.getDimension(attr, clearIconSize.toFloat()).toInt()
                }
                R.styleable.ClearEditText_cetClearIconWidth -> {
                    clearIconWidth = typedArray.getDimension(attr, clearIconWidth.toFloat()).toInt()
                }
                R.styleable.ClearEditText_cetClearIconHeight -> {
                    clearIconHeight = typedArray.getDimension(attr, clearIconHeight.toFloat()).toInt()
                }
                R.styleable.ClearEditText_cetHintIconSize -> {
                    hintIconSize = typedArray.getDimension(attr, hintIconSize.toFloat()).toInt()
                }
                R.styleable.ClearEditText_cetHintIconWidth -> {
                    hintIconWidth = typedArray.getDimension(attr, hintIconWidth.toFloat()).toInt()
                }
                R.styleable.ClearEditText_cetClearIconHeight -> {
                    hintIconHeight = typedArray.getDimension(attr, hintIconHeight.toFloat()).toInt()
                }
            }
        }

        onFocusChangeListener = this
        addTextChangedListener(this)
        compoundDrawablePadding = iconMarginLeft.toInt()
        setDisabled(disabled)
    }

    /**
     * 设置清除图标
     */
    private fun setClearIcon() {
        if (clearIcon != null && showClearIcon && !disabled && hasFocus) {
            compoundDrawables?.let {
                clearIcon!!.setBounds(0, 0, clearIconWidth, clearIconHeight)
                setCompoundDrawables(null, null, clearIcon, null)
                isClearShow = true
            }
        }
    }

    /**
     * 设置提醒图标
     */
    private fun setHintIcon() {
        if (hintIcon != null && showHintIcon && !disabled) {
            compoundDrawables?.let {
                hintIcon!!.setBounds(0, 0, hintIconWidth, hintIconHeight)
                setCompoundDrawables(null, null, hintIcon, null)
            }
        }
    }

    /**
     * 清除图标
     */
    private fun clearAllIcon() {
        isClearShow = false
        compoundDrawables?.let {
            setCompoundDrawables(null, null, null, null)
        }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        this.hasFocus = hasFocus
        val inputText = text?.toString()
        if (hasFocus) {
            if (!TextUtils.isEmpty(inputText)) {
                setClearIcon()
            } else {
                clearAllIcon()
            }
        } else {
            if (require && TextUtils.isEmpty(inputText)) {
                setHintIcon()
            } else {
                clearAllIcon()
            }
        }

    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        onTextChangeListener?.beforeTextChanged(s, start, count, after)
    }


    override fun afterTextChanged(s: Editable?) {
        onTextChangeListener?.afterTextChanged(s)
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        clearAllIcon()
        if (!TextUtils.isEmpty(text?.toString())) {
            setClearIcon()
        }
        onTextChangeListener?.onTextChanged(text, start, lengthBefore, lengthAfter)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isClearShow && event.action == MotionEvent.ACTION_UP) {
            if (width - compoundPaddingRight < event.x && width - paddingRight > event.x) {
                setText("")
                clearAllIcon()
                onClearListener?.onClear()
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 设置清除图标
     */
    fun setClearDrawable(drawable: Drawable?) {
        this.clearIcon = drawable
        setClearIcon()
    }

    /**
     * 设置提示图标
     */
    fun setHintDrawable(drawable: Drawable?) {
        this.hintIcon = drawable
        setHintIcon()
    }

    /**
     * 设置输入框是否不可用
     */
    fun setDisabled(disabled: Boolean) {
        this.disabled = disabled
        isEnabled = !disabled
        isFocusable = !disabled
        isFocusableInTouchMode = !disabled
        clearAllIcon()
        disabledTextColor?.let { setTextColor(it) }
    }

    /**
     * 设置数据变动监听
     */
    fun setOnTextChangeListener(onTextChangeListener: OnTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener
    }

    /**
     * 设置数据清除回调
     */
    fun setOnClearListener(onClearListener: OnClearListener) {
        this.onClearListener = onClearListener
    }

}