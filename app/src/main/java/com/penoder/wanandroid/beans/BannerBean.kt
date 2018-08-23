package com.penoder.wanandroid.beans

import com.penoder.mylibrary.okhttp.BaseEntity


/**
 * @author Penoder
 * @date 2018/8/24
 */
class BannerBean : BaseEntity() {

    var id: Int = -1
    var title: String = ""
    var desc: String = ""
    var imagePath: String = ""
    var url: String = ""
    var isVisible: Byte = 1
    var order: Int = 0
    var type: Int = 0

}