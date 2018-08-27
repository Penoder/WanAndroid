package com.penoder.wanandroid.ui.base

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.penoder.wanandroid.BR

/**
 * 所有类默认是 final 修饰，不允许集成，可以使用 open 修饰
 *
 * @author Penoder
 * @date 2018/8/19
 */
abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    private var binding: V? = null
    protected var mContext: Context? = null
    private var lastClickTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        binding = DataBindingUtil.setContentView(this, getLayoutID())
        val viewModel = createViewModel()
        binding?.setVariable(BR.viewModel, viewModel ?: this)
        initialView()
        initialData()
    }

    protected fun getBinding(): V? {
        return binding
    }

    /**
     * 方法默认被 final 修饰，所以之类不能继承
     */
    protected open fun createViewModel(): IViewModel? {
        return null
    }

    protected abstract fun getLayoutID(): Int

    protected open fun initialView() {
    }

    protected open fun initialData() {
    }

    protected fun isFastClick(): Boolean {
        return isFastClick(500)
    }

    /**
     * 检测是否快速点击
     */
    protected fun isFastClick(millSec: Int): Boolean {
        if (System.currentTimeMillis() - lastClickTime < millSec) {
            return true
        }
        lastClickTime = System.currentTimeMillis()
        return false
    }

}