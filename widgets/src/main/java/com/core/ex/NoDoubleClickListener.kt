package com.core.ex

import android.view.View

/**
 * 防止连续点击
 * @author like
 * @date 2020/10/13 10:10 AM
 */
abstract class NoDoubleClickListener : View.OnClickListener {

    private var lastClickTime: Long = 0

    protected abstract fun onNoDoubleClick(v: View)

    companion object {
        var MIN_CLICK_DELAY_TIME = 500L
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }
}

// 为 View 添加方法，以后使用这个方法来替换 setOnClickListener 就可以了
fun View.onDebouncedClick(click: (view: View) -> Unit) {
    setOnClickListener(object : NoDoubleClickListener() {
        override fun onNoDoubleClick(v: View) {
            click(v)
        }
    })
}