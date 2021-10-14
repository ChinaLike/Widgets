package com.test.widgets

import android.content.Intent
import android.os.Bundle
import com.core.ex.onDebouncedClick
import com.test.widgets.databinding.ActivityMainBinding


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //导航栏
        binding.toolbarBtn.onDebouncedClick {

        }
        //分页网格
        binding.pagerGridBtn.onDebouncedClick {
            startActivity(Intent(this, PagerGridActivity::class.java))
        }
    }

}