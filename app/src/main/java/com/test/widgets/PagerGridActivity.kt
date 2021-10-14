package com.test.widgets

import android.os.Bundle
import android.widget.Toast
import com.core.widget.pager.OnItemClickListener
import com.core.widget.pager.PageChangedListener
import com.test.widgets.adapter.PagerGridAdapter
import com.test.widgets.databinding.ActivityPagerGridBinding

class PagerGridActivity : BaseActivity<ActivityPagerGridBinding>(),OnItemClickListener,PageChangedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //代码设置属性
        binding.codePagerGridView.apply {
            rows = 4
            columns = 2
            horizontalDivider = 10
            verticalDivider = 10
            onItemClickListener = this@PagerGridActivity
            setAdapter(PagerGridAdapter(this@PagerGridActivity, getTestData(20)))
        }

        //XML设置属性
        binding.xmlPagerGridView.apply {
            onItemClickListener = this@PagerGridActivity
            setAdapter(PagerGridAdapter(this@PagerGridActivity,getTestData(40)))
        }

        //默认选中
        binding.defaultSelectPagerGridView.apply {
            onItemClickListener = this@PagerGridActivity
            setAdapter(PagerGridAdapter(this@PagerGridActivity,getTestData(60)))
        }

        //指示器
        binding.indicatorSelectPagerGridView.apply {
            onItemClickListener = this@PagerGridActivity
            setAdapter(PagerGridAdapter(this@PagerGridActivity,getTestData(40)))
        }

    }

    private fun getTestData(size: Int): MutableList<String> {
        val list = mutableListOf<String>()
        for (i in 0 until size) {
            list.add("数据-$i")
        }
        return list
    }

    /**
     * Item项被点击了
     */
    override fun onItemClick(position: Int) {
        Toast.makeText(this,"第${position}项被选中了",Toast.LENGTH_SHORT).show()
    }

    /**
     * 页面总数量变化
     * @param [pageSize] 总数
     */
    override fun onPageSizeChanged(pageSize: Int) {

    }

    /**
     * 页面被选中
     * @param [pageIndex] 选中的下标
     */
    override fun onPageSelect(pageIndex: Int) {
        Toast.makeText(this,"第${pageIndex}页被选中了",Toast.LENGTH_SHORT).show()
    }
}