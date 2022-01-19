package com.test.widgets

import android.os.Bundle
import android.widget.Toast
import com.core.widget.toolbar.OnToolbarListener
import com.test.widgets.databinding.ActivityToolbarBinding

class ToolbarActivity : BaseActivity<ActivityToolbarBinding>(), OnToolbarListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.simpleToolbar.onToolbarListener = this
        binding.searchToolbar.onToolbarListener = this
        binding.searchToolbar.showKeyboard()
        binding.searchToolbar1.onToolbarListener = this
        binding.searchToolbar1.setSearchText("输入框无法点击输入")

        binding.searchToolbar2.setSearchHintText("dddjdd")
        binding.searchToolbar2.onToolbarListener = object : OnToolbarListener {
            override fun onMiddleLayoutClick(text: String?) {
                binding.searchToolbar2.setSearchText("王者")
                Toast.makeText(this@ToolbarActivity, "中间布局被点击：${text}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onMiddleLayoutClick(text: String?) {
        Toast.makeText(this, "中间布局被点击：${text}", Toast.LENGTH_SHORT).show()
    }
}