package com.test.widgets

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.core.widget.toolbar.OnToolbarListener
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
                window.setAttributes(attributes)
                getWindow().statusBarColor = Color.TRANSPARENT
            } else {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.getAttributes()
                attributes.flags = attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                window.setAttributes(attributes)
            }
        }

        super.onCreate(savedInstanceState)
        val binding   = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}