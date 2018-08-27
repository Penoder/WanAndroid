package com.penoder.wanandroid.ui.activity

import android.content.Context
import android.os.Bundle
import com.penoder.wanandroid.R
import com.penoder.wanandroid.databinding.ActivityWebBinding
import com.penoder.wanandroid.ui.base.BaseActivity

class WebActivity : BaseActivity<ActivityWebBinding>() {

    override fun getLayoutID(): Int = R.layout.activity_web

    var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = mContext
    }

    companion object {
        fun startSelf(cls: Class<Any>) {

        }
    }

}