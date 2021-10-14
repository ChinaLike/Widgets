package com.core.widget.pager

/**
 * Item点击回调
 * @author like
 * @date 10/13/21 2:16 PM
 */
internal interface OnItemClickCallback {

    /**
     * Item点击
     * @param [position] 当前页下标
     * @param [page] 当前页
     */
    fun onItemClick(position: Int,page:Int)

}