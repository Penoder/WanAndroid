package com.penoder.wanandroid.beans

/**
 * 首页文章列表实体类
 *
 * @author Penoder
 * @date 2018/8/22
 */
class ArticleListBean : BaseEntity() {

    var curPage: Int = 0    // 当前页面
    var datas: ArrayList<ArticleBean> = ArrayList()  // 文章数据集合
    var offset: Int = 0
    var over: Boolean = false   // 时候加载完
    var pageCount: Int = 0  // 页面总数
    var size: Int = 0
    var total: Int = 0  // 文章总数

}