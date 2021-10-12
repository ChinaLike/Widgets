package com.test.widgets

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.core.widget.recyclerview.PagerGridLayoutManager
import com.core.widget.recyclerview.PagerGridSnapHelper
import com.test.widgets.adapter.PagerGridAdapter
import com.test.widgets.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.getAttributes()
                attributes.flags = attributes.flags or flagTranslucentNavigation
                window.attributes = attributes
                getWindow().statusBarColor = Color.TRANSPARENT
            } else {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.getAttributes()
                attributes.flags = attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                window.attributes = attributes
            }
        }

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = PagerGridLayoutManager(4, 4)
        PagerGridSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.adapter = PagerGridAdapter(this, getData())


    }

    private fun getData(): MutableList<Int> {
        val list = mutableListOf<Int>()
        for (i in 0 until 12) {
            list.add(i)
        }
        return list
    }

}