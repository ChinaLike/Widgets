package com.core.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.core.util.DensityUtil

/**
 * 绘制三角形
 * @author like
 * @date 9/1/21 3:25 PM
 */
class TriangleView : View {

    private var mPaint: Paint? = null
    private var mColor = 0
    private var mWidth = 0
    private var mHeight = 0
    private var mDirection = 0
    private var mPath: Path? = null

    companion object {
        const val TOP = 0
        const val BOTTOM = 1
        const val RIGHT = 2
        const val LEFT = 3
        const val DEFAULT_WIDTH = 10F
        const val DEFAULT_HEIGHT = 6F
        var DEFAULT_COLOR: Int = Color.WHITE
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
        val typedArray = context.theme.obtainStyledAttributes(attributeSet, R.styleable.TriangleView, 0, 0)
        mColor = typedArray.getColor(R.styleable.TriangleView_lk_color, DEFAULT_COLOR)
        mDirection = typedArray.getInt(R.styleable.TriangleView_lk_direction, mDirection)
        typedArray.recycle()
        mPaint!!.color = mColor
    }

    private fun init() {
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.style = Paint.Style.FILL
        mPath = Path()
        mDirection = TOP
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (mWidth == 0 || widthMode != MeasureSpec.EXACTLY) {
            mWidth = DensityUtil.dp2px(DEFAULT_WIDTH)
        }
        if (mHeight == 0 || heightMode != MeasureSpec.EXACTLY) {
            mHeight = DensityUtil.dp2px(DEFAULT_HEIGHT)
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        when (mDirection) {
            TOP -> {
                mPath!!.moveTo(0f, mHeight.toFloat())
                mPath!!.lineTo(mWidth.toFloat(), mHeight.toFloat())
                mPath!!.lineTo((mWidth / 2).toFloat(), 0f)
            }
            BOTTOM -> {
                mPath!!.moveTo(0f, 0f)
                mPath!!.lineTo((mWidth / 2).toFloat(), mHeight.toFloat())
                mPath!!.lineTo(mWidth.toFloat(), 0f)
            }
            RIGHT -> {
                mPath!!.moveTo(0f, 0f)
                mPath!!.lineTo(0f, mHeight.toFloat())
                mPath!!.lineTo(mWidth.toFloat(), (mHeight / 2).toFloat())
            }
            LEFT -> {
                mPath!!.moveTo(0f, (mHeight / 2).toFloat())
                mPath!!.lineTo(mWidth.toFloat(), mHeight.toFloat())
                mPath!!.lineTo(mWidth.toFloat(), 0f)
            }
            else -> {
            }
        }

        mPath!!.close()
        canvas!!.drawPath(mPath!!, mPaint!!)
    }
}