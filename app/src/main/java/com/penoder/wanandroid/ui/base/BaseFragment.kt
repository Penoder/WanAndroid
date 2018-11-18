package com.penoder.wanandroid.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.penoder.wanandroid.BR


/**
 * @author Penoder
 * @date 2018/8/19
 */
abstract class BaseFragment<V : ViewDataBinding> : Fragment() {

    private var binding: V? = null
    protected var mContext: Context? = null
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        binding?.executePendingBindings()
        val viewModel = createViewModel()
        binding?.setVariable(BR.viewModel, viewModel ?: this)
        initialView()
        initialData()
        return binding?.root
    }

    protected fun getBinding() : V? = binding

    protected abstract fun getLayoutID(): Int

    protected open fun createViewModel(): IViewModel? {
        return null
    }

    protected open fun initialView() {
    }

    protected open fun initialData() {
    }

    protected fun isFastClick(): Boolean {
        return isFastClick(500)
    }

    protected fun isFastClick(millSec: Int): Boolean {
        if (System.currentTimeMillis() - lastClickTime < millSec) {
            return true
        }
        lastClickTime = System.currentTimeMillis()
        return false
    }
}