package com.penoder.wanandroid.ui.viewModel

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.penoder.mylibrary.okhttp.BaseEntity
import com.penoder.mylibrary.okhttp.OkCallBack
import com.penoder.mylibrary.okhttp.OkHttpManager
import com.penoder.mylibrary.utils.LogUtil
import com.penoder.mylibrary.utils.ToastUtil
import com.penoder.wanandroid.adapter.HomeAdapter
import com.penoder.wanandroid.databinding.FragmentHomeBinding
import com.penoder.wanandroid.beans.ArticleBean
import com.penoder.wanandroid.beans.ArticleListBean
import com.penoder.wanandroid.constants.WanApi
import com.penoder.wanandroid.ui.base.IViewModel
import com.google.gson.reflect.TypeToken;

/**
 * @author Penoder
 * @date 2018/8/20
 */
class HomeViewModel(context: Context?, binding: FragmentHomeBinding?) : IViewModel {

    private var mContext: Context? = context
    private var mBinding: FragmentHomeBinding? = binding
    var pageNum: Int = 0
    private var homeAdapter: HomeAdapter? = null
    private var articleList: ArrayList<ArticleBean> = ArrayList()

    init {
        homeAdapter = HomeAdapter(articleList)
        binding?.recyclerHome?.isFocusable = false
        binding?.recyclerHome?.layoutManager = object : LinearLayoutManager(mContext) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding?.recyclerHome?.adapter = homeAdapter
    }

    fun getBannerInfo() {
        val type = object : TypeToken<List<BaseEntity>>() {}.getType()
        OkHttpManager.build(mContext)
                .addUrl(WanApi.HOME_BANNER)
                .execute(OkCallBack<List<BaseEntity>> { isSuccess, data ->
                    if (isSuccess) {
                        LogUtil.i("banner: -- $data")
                    } else {
                        LogUtil.i("69666666666666666")
                    }
                }, type)
    }

    fun getArticleList() {
        OkHttpManager.build(mContext)
                .addUrl(WanApi.ARTICLE_LIST + pageNum + WanApi.RESPONSE_FORMAT)
                .execute(OkCallBack<ArticleListBean> { isSuccess, articleListBean ->
                    if (isSuccess) {
                        if (pageNum == 0) {
                            articleList.clear()
                        }
                        if (articleListBean?.datas != null && !articleListBean.datas.isEmpty()) {
                            articleList.addAll(articleListBean.datas)
                            homeAdapter?.notifyDataSetChanged()
                        }
                        pageNum++
                    } else {
                        ToastUtil.showShortToast(mContext, "66666")
                    }
                    if (mBinding?.refreshHome?.isRefreshing!!)
                        mBinding?.refreshHome?.finishRefresh(isSuccess)
                    if (mBinding?.refreshHome?.isLoading!!)
                        mBinding?.refreshHome?.finishLoadmore(isSuccess)
                }, ArticleListBean())
    }

}