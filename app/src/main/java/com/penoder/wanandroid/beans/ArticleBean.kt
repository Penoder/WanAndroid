package com.penoder.wanandroid.beans

import com.penoder.mylibrary.okhttp.BaseEntity


/**
 * 首页文章列表对应文章实体类
 * @author Penoder
 * @date 2018/8/22
 */
class ArticleBean : BaseEntity() {

    var apkLink: String = ""
    var author: String = ""
    var chapterId: Int = -1
    var chapterName: String = ""
    var collect: Boolean = false
    var courseId: Int = -1
    var desc: String = ""
    var envelopePic: String = ""
    var fresh: Boolean = false
    var id: Int = -1
    var link: String = ""
    var niceDate: String = ""
    var origin: String = ""
    var projectLink: String = ""
    var publishTime: Long = 0
    var superChapterId: Int = -1
    var superChapterName: String = ""
    var title: String = ""
    var type: Int = 0
    var userId: Int = -1
    var visible: Int = 1
    var zan: Int = 0
    var tags: List<TagBean>? = null

    class TagBean {
        var name: String = ""
        var url: String = ""
    }

}