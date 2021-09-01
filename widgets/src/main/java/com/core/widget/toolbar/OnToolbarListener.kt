package com.core.widget.toolbar

import androidx.appcompat.widget.AppCompatImageView

/**
 * Toolbar监听
 * @author like
 * @date 8/30/21 10:15 PM
 */
interface OnToolbarListener {

    /**
     * 返回键监听事件
     * @return true 点击事件生效，允许返回 ， false 点击事件无效，不允许返回
     */
    fun onCallback(): Boolean {
        return true
    }

    /**
     * 关闭文本按钮
     */
    fun onClose() {

    }

    /**
     * 菜单按钮被点击，快速定义的菜单
     */
    fun onMenuClick(view: AppCompatImageView) {

    }

    /**
     * 搜索框输入后搜索
     */
    fun search(searchText:String?){

    }

    /**
     * 搜索内容变化
     */
    fun onSearchTextChanged(searchText:String?){

    }

    /**
     * 搜索取消
     * @return true 取消后切换为搜索图标且变化为标题 false 不变化
     */
    fun onSearchCancel():Boolean{
        return true
    }

}