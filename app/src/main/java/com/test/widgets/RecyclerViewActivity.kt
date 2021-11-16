package com.test.widgets

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.core.widget.divider.XItemDecoration
import com.test.widgets.adapter.DividerAdapter
import com.test.widgets.databinding.ActivityRecyclerViewBinding

class RecyclerViewActivity : BaseActivity<ActivityRecyclerViewBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding.recyclerView.layoutManager = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false)
//        binding.recyclerView.addItemDecoration(XItemDecoration(resources.getDrawable(R.drawable.divider_divider)).apply {
//
////            drawFirstRowBefore(true, Color.BLUE)
////            drawFirstColBefore(true, Color.GRAY)
////            drawLastRowAfter(true, Color.GREEN)
////            drawLastColAfter(true, Color.DKGRAY)
////            dividerRowAndColHeight(30,30)
//            horizontalSpace = 30
//            isDrawFirstRow = true
//            isDrawFirstColumn = true
//            isDrawLastRow = true
//
//        })
//        binding.recyclerView.adapter = DividerAdapter()

        binding.recyclerView1.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        binding.recyclerView1.addItemDecoration(XItemDecoration(30, 30, Color.RED).apply {
//            showBorder = false
//            isDrawFirstRow = true
            dividerDrawable = resources.getDrawable(R.drawable.divider_divider)
//            topRowSpace = 30
        })
        binding.recyclerView1.adapter = DividerAdapter()

        val adapter = DividerAdapter()
        binding.addData.setOnClickListener {

        }
        binding.recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView2.addItemDecoration(XItemDecoration(30, Color.BLUE).apply {
//            isDrawLastDivider = true
            isDrawFirstRow = true
            topRowSpace = 20
            topRowColor = Color.GREEN

            isDrawFirstColumn = true
            leftColumnSpace = 50
            leftColumnColor = Color.BLACK
        })
        binding.recyclerView2.adapter = adapter

    }
}