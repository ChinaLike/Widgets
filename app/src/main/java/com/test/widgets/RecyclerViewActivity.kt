package com.test.widgets

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.widget.divider.GridItemDecoration
import com.core.widget.divider.XItemDecoration
import com.test.widgets.adapter.DividerAdapter
import com.test.widgets.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : BaseActivity<ActivityRecyclerViewBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.recyclerView.layoutManager = GridLayoutManager(this,3,GridLayoutManager.VERTICAL,false)
        binding.recyclerView.addItemDecoration(GridItemDecoration(RecyclerView.VERTICAL).apply {
            dividerColor(Color.RED)
            dividerHeight(10,30)
//            drawFirstRowBefore(true, Color.BLUE)
//            drawFirstColBefore(true, Color.GRAY)
//            drawLastRowAfter(true, Color.GREEN)
//            drawLastColAfter(true, Color.DKGRAY)
//            dividerRowAndColHeight(30,30)
        })
        binding.recyclerView.adapter = DividerAdapter()

        binding.recyclerView1.layoutManager = GridLayoutManager(this,3,RecyclerView.VERTICAL,false)
        binding.recyclerView1.addItemDecoration(XItemDecoration(10,30,Color.RED).apply {
            showBorder = true

        })
        binding.recyclerView1.adapter = DividerAdapter()


        binding.recyclerView2.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        binding.recyclerView2.addItemDecoration(XItemDecoration(30,Color.RED).apply {
            isDrawLastDivider = true
        })
        binding.recyclerView2.adapter = DividerAdapter()

    }
}