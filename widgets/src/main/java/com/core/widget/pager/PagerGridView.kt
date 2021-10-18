package com.core.widget.pager

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager2.widget.ViewPager2
import com.core.widget.R
import com.core.widget.indicator.BaseIndicator
import com.core.widget.indicator.CircleIndicator
import com.core.widget.indicator.IndicatorConfig

/**
 * 带分页的网格布局
 * @author like
 * @date 10/12/21 6:09 PM
 */
class PagerGridView : LinearLayout, OnItemClickCallback {

    /**
     * 界面数据
     */
    private val pageDataList: MutableList<MutableList<View>> = mutableListOf()

    /**
     * 滑动组件
     */
    private val viewPager: ViewPager2 = ViewPager2(context)

    /**
     * 指示器
     */
    private val indicatorView: BaseIndicator = CircleIndicator(context)

    /**
     * 界面适配器
     */
    private val pagerAdapter: PagerAdapter = PagerAdapter(context, pageDataList)

    /**
     * 行
     */
    var rows: Int = 0

    /**
     * 列
     */
    var columns: Int = 0

    /**
     * 总页数
     */
    private var pageSize: Int = 0

    /**
     * 水平分割线宽度
     */
    var horizontalDivider: Int = 0

    /**
     * 垂直分割线宽度
     */
    var verticalDivider: Int = 0

    /**
     * 设置滚动方向
     */
    var orientationType = ViewPager2.ORIENTATION_HORIZONTAL

    /**
     * 一页显示Item数量
     */
    private var onePageSize: Int = 0

    /**
     * Page变化监听
     */
    var pageChangedListener: PageChangedListener? = null

    /**
     * Item点击监听
     */
    var onItemClickListener: OnItemClickListener? = null

    /**
     * 默认选中页面
     */
    var defaultSelectPageIndex: Int = 0

    /**
     * 是否显示指示器
     */
    var showIndicator: Boolean = false

    /**
     * 存放单个View
     */
    private val viewList: MutableList<View> = mutableListOf()

    /**
     * 存放分页View
     */
    private val pageView: MutableList<View> = mutableListOf()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attrs?.let { initAttrs(context, it) }
        initView()
        //注册界面滚动监听
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageChangedListener?.onPageSelect(position)
                if (showIndicator) {
                    indicatorView.onPageSelect(position)
                }
            }

        })
    }

    /**
     * 获取布局中设置属性
     */
    private fun initAttrs(context: Context, attrs: AttributeSet) = with(context.obtainStyledAttributes(attrs, R.styleable.PagerGridView)) {
        val indicatorConfig = indicatorView.indicatorConfig()
        for (i in 0 until indexCount) {
            when (val attr = getIndex(i)) {
                R.styleable.PagerGridView_lk_pager_grid_row -> {
                    rows = getInt(attr, rows)
                }
                R.styleable.PagerGridView_lk_pager_grid_column -> {
                    columns = getInt(attr, columns)
                }
                R.styleable.PagerGridView_lk_pager_grid_orientation -> {
                    orientationType = getInt(attr, orientationType)
                }
                R.styleable.PagerGridView_lk_pager_grid_horizontal_divider -> {
                    horizontalDivider = getDimension(attr, horizontalDivider.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_pager_grid_vertical_divider -> {
                    verticalDivider = getDimension(attr, verticalDivider.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_pager_grid_default_select -> {
                    defaultSelectPageIndex = getInt(attr, defaultSelectPageIndex)
                }
                R.styleable.PagerGridView_lk_pager_grid_show_indicator -> {
                    showIndicator = getBoolean(attr, showIndicator)
                }
                R.styleable.PagerGridView_lk_indicator_color -> {
                    indicatorConfig.normalColor = getColor(attr, indicatorConfig.normalColor)
                }
                R.styleable.PagerGridView_lk_indicator_select_color -> {
                    indicatorConfig.selectedColor = getColor(attr, indicatorConfig.selectedColor)
                }
                R.styleable.PagerGridView_lk_indicator_type -> {
                    indicatorConfig.type = getInt(attr, indicatorConfig.type)
                }
                R.styleable.PagerGridView_lk_indicator_width -> {
                    indicatorConfig.normalWidth = getDimension(attr, indicatorConfig.normalWidth.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_height -> {
                    indicatorConfig.normalHeight = getDimension(attr, indicatorConfig.normalHeight.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_selected_width -> {
                    indicatorConfig.selectedWidth = getDimension(attr, indicatorConfig.selectedWidth.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_selected_height -> {
                    indicatorConfig.selectedHeight = getDimension(attr, indicatorConfig.selectedHeight.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_space -> {
                    indicatorConfig.indicatorSpace = getDimension(attr, indicatorConfig.indicatorSpace.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_radius -> {
                    indicatorConfig.radius = getDimension(attr, indicatorConfig.radius.toFloat()).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_gravity -> {
                    indicatorConfig.gravity = getInt(attr, indicatorConfig.gravity)
                }
                R.styleable.PagerGridView_lk_indicator_margin -> {
                    val margin = getDimension(attr, 0F).toInt()
                    indicatorConfig.margins.leftMargin = margin
                    indicatorConfig.margins.topMargin = margin
                    indicatorConfig.margins.rightMargin = margin
                    indicatorConfig.margins.bottomMargin = margin
                }
                R.styleable.PagerGridView_lk_indicator_margin_left -> {
                    indicatorConfig.margins.leftMargin = getDimension(attr, 0F).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_margin_top -> {
                    indicatorConfig.margins.topMargin = getDimension(attr, 0F).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_margin_right -> {
                    indicatorConfig.margins.rightMargin = getDimension(attr, 0F).toInt()
                }
                R.styleable.PagerGridView_lk_indicator_margin_bottom -> {
                    indicatorConfig.margins.bottomMargin = getDimension(attr, 0F).toInt()
                }
            }
        }
        recycle()
    }

    /**
     * 初始化
     */
    private fun initView() {
        orientation = VERTICAL
        viewPager.apply {
            orientation = orientationType
        }
        addView(viewPager)
        if (showIndicator) {
            addView(indicatorView)
            val layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.gravity =
                when (indicatorView.indicatorConfig().gravity) {
                    IndicatorConfig.Direction.LEFT -> Gravity.LEFT
                    IndicatorConfig.Direction.RIGHT -> Gravity.RIGHT
                    else -> Gravity.CENTER
                }
            layoutParams.leftMargin = indicatorView.indicatorConfig().margins.leftMargin
            layoutParams.topMargin = indicatorView.indicatorConfig().margins.topMargin
            layoutParams.rightMargin = indicatorView.indicatorConfig().margins.rightMargin
            layoutParams.bottomMargin = indicatorView.indicatorConfig().margins.bottomMargin
            indicatorView.layoutParams = layoutParams
        }
    }

    /**
     * 设置适配器
     */
    fun setAdapter(adapter: PagerGridViewAdapter) {
        if (rows == 0 || columns == 0) {
            return
        }
        calculatePageTotal(adapter.getItemCount())
        //拿到所有View
        viewList.clear()
        for (i in 0 until adapter.getItemCount()) {
            val childView = adapter.onBindView(i)
            viewList.add(childView)
        }
        //分配到对应每一页
        pageView.clear()
        splitList()?.let {
            if (pageSize > 0) {
                viewPager.offscreenPageLimit = pageSize
            }
            if (showIndicator) {
                indicatorView.onPageSizeChanged(pageSize)
            }
            pageChangedListener?.onPageSizeChanged(pageSize)
            pagerAdapter?.apply {
                spanSize = rows
                onItemClickCallback = this@PagerGridView
                horizontalDivider = this@PagerGridView.horizontalDivider
                verticalDivider = this@PagerGridView.verticalDivider
            }
            viewPager.adapter = pagerAdapter
            viewPager.currentItem = defaultSelectPageIndex
        }
    }

    /**
     * 计算Page总数
     */
    private fun calculatePageTotal(count: Int): Int {
        this.onePageSize = rows * columns
        var pageSize = count / (this.onePageSize)
        if (count % (this.onePageSize) != 0) {
            pageSize++
        }
        this.pageSize = pageSize
        return pageSize
    }

    /**
     * 切割数组
     */
    private fun splitList(): MutableList<MutableList<View>> {
        pageDataList.clear()
        viewList?.let {
            var startIndex = 0
            var endIndex = 0
            while (startIndex < it.size) {
                endIndex = startIndex + onePageSize
                if (endIndex > it.size) {
                    endIndex = it.size
                }
                val childList = it.subList(startIndex, endIndex)
                pageDataList.add(childList)
                startIndex += onePageSize
            }
        }
        return pageDataList
    }

    /**
     * Item点击
     * @param [position] 当前页下标
     * @param [page] 当前页
     */
    override fun onItemClick(position: Int, page: Int) {
        val index = page * onePageSize + position
        onItemClickListener?.onItemClick(index)
        onItemClickListener?.onItemClick(index, page)
    }

}