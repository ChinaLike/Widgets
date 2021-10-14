package com.test.widgets.adapter

import android.app.Activity
import android.view.View
import com.core.widget.pager.PagerGridViewAdapter
import com.test.widgets.databinding.AdapterPagerGridBinding

/**
 * 分页适配器
 * @author like
 * @date 10/13/21 4:44 PM
 */
class PagerGridAdapter(private val activity: Activity,private val dataList:MutableList<String>):PagerGridViewAdapter() {
    /**
     * 获取Item的数量
     */
    override fun getItemCount(): Int {
        return dataList.size
    }

    /**
     * 绑定数据
     */
    override fun onBindView(position: Int): View {
        val binding = AdapterPagerGridBinding.inflate(activity.layoutInflater)
        val bean = dataList[position]
        binding.name.text = "$bean"
        return binding.root
    }
}