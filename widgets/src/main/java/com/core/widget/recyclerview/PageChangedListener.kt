package com.core.widget.recyclerview

/**
 *
 * @author like
 * @date 10/12/21 10:07 AM
 */
interface PageChangedListener {

    /**
     * 页面总数量变化
     * @param [pageSize] 总数
     */
    fun onPageSizeChanged(pageSize:Int)

    /**
     * 页面被选中
     * @param [pageIndex] 选中的下标
     */
    fun onPageSelect(pageIndex:Int)

}