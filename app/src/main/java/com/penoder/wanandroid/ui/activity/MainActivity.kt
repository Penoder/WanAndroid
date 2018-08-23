package com.penoder.wanandroid.ui.activity

import android.support.v4.app.Fragment
import com.penoder.mylibrary.adapter.CommonFragmentAdapter
import com.penoder.wanandroid.R
import com.penoder.wanandroid.databinding.ActivityMainBinding
import com.penoder.wanandroid.ui.base.BaseActivity
import com.penoder.wanandroid.ui.fragment.HomeFragment
import com.penoder.wanandroid.ui.fragment.SettingFragment
import com.penoder.wanandroid.ui.fragment.SystemFragment
import com.penoder.wanandroid.ui.fragment.TodoFragment
import com.penoder.wanandroid.ui.viewModel.MainViewModel

/**
 * @author Penoder
 * @date 2018/8/19
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private var homeFragment = HomeFragment()
    private var todoFragment = TodoFragment()
    private var systemFragment = SystemFragment()
    private var settingFragment = SettingFragment()

    private var fragments = ArrayList<Fragment>()

    override fun getLayoutID(): Int {
        return R.layout.activity_main
    }

    override fun createViewModel(): MainViewModel {
        return MainViewModel(getBinding())
    }

    override fun initialView() {
        super.initialView()
        fragments.add(homeFragment)
        fragments.add(todoFragment)
        fragments.add(systemFragment)
        fragments.add(settingFragment)
        val adapter = CommonFragmentAdapter(supportFragmentManager, fragments)
        getBinding()?.viewPagerMain?.adapter = adapter
        getBinding()?.viewPagerMain?.offscreenPageLimit = 4
        // 默认第一个选中
        getBinding()?.viewModel?.setSelectTab(0)
    }
}
