package com.penoder.wanandroid.ui.viewModel

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import com.penoder.mylibrary.okhttp.OkCallBack
import com.penoder.mylibrary.okhttp.OkHttpManager
import com.penoder.mylibrary.utils.ToastUtil
import com.penoder.wanandroid.adapter.HomeAdapter
import com.penoder.wanandroid.databinding.FragmentHomeBinding
import com.penoder.wanandroid.beans.ArticleBean
import com.penoder.wanandroid.beans.ArticleListBean
import com.penoder.wanandroid.constants.WanApi
import com.penoder.wanandroid.ui.base.IViewModel
import com.google.gson.reflect.TypeToken
import com.penoder.mylibrary.utils.ImgLoadUtil
import com.penoder.wanandroid.beans.BannerBean
import com.penoder.wanandroid.ui.activity.WebActivity

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
        homeAdapter?.setOnItemClickListener(object : HomeAdapter.OnItemClickListener {
            override fun onItemClick(articleBean: ArticleBean?) {
                WebActivity.startSelf(mContext, articleBean?.link)
            }
        })
        binding?.recyclerHome?.isFocusable = false
        binding?.recyclerHome?.layoutManager = object : LinearLayoutManager(mContext) {
            override fun canScrollVertically(): Boolean {
                return false
            }
        }
        binding?.recyclerHome?.adapter = homeAdapter
        binding?.bannerHome?.setAdapter { _, itemView, model, _ ->
            if (model != null) {
                val bannerBean: BannerBean = model as BannerBean
                ImgLoadUtil.loadImg(bannerBean.imagePath, itemView as ImageView?)
                itemView.setOnClickListener {
                    WebActivity.startSelf(mContext, bannerBean.url)
                }
            }
        }
    }

    fun getBannerInfo() {
        val type = object : TypeToken<List<BannerBean>>() {}.getType()
        OkHttpManager.build(mContext)
                .addUrl(WanApi.HOME_BANNER)
                .execute(OkCallBack<List<BannerBean>> { isSuccess, data: List<BannerBean> ->
                    if (isSuccess) {
                        val titleList = ArrayList<String>()
                        for (item in data) {
                            titleList.add(item.title)
                        }
                        mBinding?.bannerHome?.setData(data, titleList)
                    } else {
                        ToastUtil.showShortToast(mContext, "233333333333333")
                    }
                }, type)
    }

    fun getArticleList() {
        OkHttpManager.build(mContext)
                .addUrl(WanApi.ARTICLE_LIST + pageNum + WanApi.RESPONSE_FORMAT)
                .execute(OkCallBack<ArticleListBean> { isSuccess, articleListBean: ArticleListBean ->
                    if (isSuccess) {
                        if (pageNum == 0) {
                            articleList.clear()
                        }
                        if (!articleListBean.datas.isEmpty()) {
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