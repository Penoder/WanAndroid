package com.penoder.wanandroid.ui.viewModel

import android.view.View
import com.penoder.wanandroid.databinding.ActivityMainBinding
import com.penoder.wanandroid.ui.base.IViewModel

/**
 * @author Penoder
 * @date 2018/8/19
 */
class MainViewModel(binding: ActivityMainBinding?) : IViewModel {

    private var mainBinding: ActivityMainBinding? = binding

    /**
     * 数据绑定的点击事件中所调用的方法或者是属性需要定义在相应的点击事件前面
     */
    fun setSelectTab(item: Int) {
        mainBinding?.viewPagerMain?.setCurrentItem(item, false)
        mainBinding?.imgViewTabHome?.isSelected = item == 0
        mainBinding?.txtViewTabHome?.isSelected = item == 0
        mainBinding?.imgViewTabTodo?.isSelected = item == 1
        mainBinding?.txtViewTabTodo?.isSelected = item == 1
        mainBinding?.imgViewTabSystem?.isSelected = item == 2
        mainBinding?.txtViewTabSystem?.isSelected = item == 2
        mainBinding?.imgViewTabSetting?.isSelected = item == 3
        mainBinding?.txtViewTabSetting?.isSelected = item == 3
    }

    fun onHomeClick(v: View) {
        setSelectTab(0)
    }

    fun onTodoClick(v: View) {
        setSelectTab(1)
    }

    fun onSystemClick(v: View) {
        setSelectTab(2)
    }

    fun onSettingClick(v: View) {
        setSelectTab(3)
    }

}