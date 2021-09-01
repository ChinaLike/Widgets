package com.test.widgets

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.core.widget.toolbar.OnToolbarListener
import com.core.widget.toolbar.ToolbarConfig
import com.test.widgets.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        ToolbarConfig.getInstance(this).apply {
            searchRadius = 10F
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.getAttributes()
                attributes.flags = attributes.flags or flagTranslucentNavigation
                window.setAttributes(attributes)
                getWindow().statusBarColor = Color.TRANSPARENT
            } else {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.getAttributes()
                attributes.flags = attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                window.setAttributes(attributes)
            }
        }

        setContentView(binding.root)

        binding.searchToolbar.onToolbarListener = object :OnToolbarListener{

            override fun onSearchCancel(): Boolean {
                Toast.makeText(this@MainActivity,"取消",Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onSearchTextChanged(searchText: String?) {
                Toast.makeText(this@MainActivity,searchText?:"",Toast.LENGTH_SHORT).show()
                super.onSearchTextChanged(searchText)
            }

            override fun search(searchText: String?) {
                Toast.makeText(this@MainActivity,searchText?:"",Toast.LENGTH_SHORT).show()
            }

        }


    }
}