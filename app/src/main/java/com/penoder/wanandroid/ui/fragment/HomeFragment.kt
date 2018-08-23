package com.penoder.wanandroid.ui.fragment

import android.os.Handler
import com.penoder.mylibrary.refresh.api.RefreshLayout
import com.penoder.mylibrary.refresh.listener.OnRefreshLoadmoreListener
import com.penoder.wanandroid.R
import com.penoder.wanandroid.databinding.FragmentHomeBinding
import com.penoder.wanandroid.ui.base.BaseFragment
import com.penoder.wanandroid.ui.base.IViewModel
import com.penoder.wanandroid.ui.viewModel.HomeViewModel


/**
 * @author Penoder
 * @date 2018/8/20
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override fun getLayoutID(): Int {
        return R.layout.fragment_home
    }

    override fun createViewModel(): IViewModel? {
        val homeViewModel = HomeViewModel(mContext, getBinding())
        homeViewModel.getBannerInfo()
        homeViewModel.getArticleList()
        return homeViewModel
    }

    override fun initialView() {
        super.initialView()

        getBinding()?.refreshHome?.setOnRefreshLoadmoreListener(object : OnRefreshLoadmoreListener {
            override fun onRefresh(refreshlayout: RefreshLayout?) {
                getBinding()?.viewModel?.pageNum = 0
                Handler().postDelayed({
                    getBinding()?.viewModel?.getArticleList()
                }, 1000)
            }

            override fun onLoadmore(refreshlayout: RefreshLayout?) {
                Handler().postDelayed({
                    getBinding()?.viewModel?.getArticleList()
                }, 1000)
            }
        })
    }
}