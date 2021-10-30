package com.test.widgets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.core.ex.onDebouncedClick
import com.test.widgets.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //导航栏
        binding.toolbarBtn.onDebouncedClick {
            startActivity(Intent(this, ToolbarActivity::class.java))
        }
        //分页网格
        binding.pagerGridBtn.onDebouncedClick {
            startActivity(Intent(this, PagerGridActivity::class.java))
        }

        //分割线
        binding.dividerBtn.onDebouncedClick {
            startActivity(Intent(this, RecyclerViewActivity::class.java))
        }
    }

}