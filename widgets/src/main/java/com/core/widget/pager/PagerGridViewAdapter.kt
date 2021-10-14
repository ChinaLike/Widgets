package com.core.widget.pager

import android.view.View

/**
 * 分页适配器
 * @author like
 * @date 10/12/21 6:15 PM
 */
abstract class PagerGridViewAdapter {

    /**
     * 获取Item的数量
     */
    abstract fun getItemCount(): Int

    /**
     * 绑定数据
     */
    abstract fun onBindView(position:Int):View

}