package com.test.widgets

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 *
 * @author like
 * @date 9/1/21 6:00 PM
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    lateinit var binding: VB

    private fun viewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        return ViewBindingUtil.viewBindingJavaClass(inflater, container, null, javaClass)
    }

    @CallSuper
     override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.attributes
                attributes.flags = attributes.flags or flagTranslucentNavigation
                window.attributes = attributes
                getWindow().statusBarColor = Color.parseColor("#03A9F4")
            } else {
                val window: Window = window
                val attributes: WindowManager.LayoutParams = window.attributes
                attributes.flags = attributes.flags or (flagTranslucentStatus or flagTranslucentNavigation)
                window.attributes = attributes
            }
        }
        super.onCreate(savedInstanceState)
        binding = viewBinding(layoutInflater, null)
        setContentView(binding.root)
    }
}