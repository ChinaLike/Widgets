package com.core.widget.pager

/**
 * Item被点击了
 * @author like
 * @date 10/13/21 2:03 PM
 */
interface OnItemClickListener {

    /**
     * Item项被点击了
     */
    fun onItemClick(position:Int)

    /**
     * Item点击
     * @param [position] 下标
     * @param [page] 当前页
     */
    fun onItemClick(position: Int,page:Int){

    }

}