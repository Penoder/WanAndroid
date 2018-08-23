package com.penoder.wanandroid.ui.fragment

import com.penoder.wanandroid.R
import com.penoder.wanandroid.databinding.FragmentTodoBinding
import com.penoder.wanandroid.ui.base.BaseFragment
import com.penoder.wanandroid.ui.base.IViewModel
import com.penoder.wanandroid.ui.viewModel.TodoViewModel


/**
 * @author Penoder
 * @date 2018/8/21
 */
class TodoFragment : BaseFragment<FragmentTodoBinding>() {

    override fun getLayoutID(): Int {
        return R.layout.fragment_todo
    }

    override fun createViewModel(): IViewModel? {
        return TodoViewModel()
    }

}