package com.test.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
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

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewBinding(layoutInflater, null)
        setContentView(binding.root)
    }
}