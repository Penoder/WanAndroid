package com.penoder.wanandroid.constants


/**
 * @author Penoder
 * @date 2018/8/19
 */
class WanApi {

    /**
     * 表示 Java 中的 static
     */
    companion object {
        private const val BASE_URL = "http://www.wanandroid.com/"
        const val RESPONSE_FORMAT = "/json"

        // 首页文章列表
        const val ARTICLE_LIST = BASE_URL + "article/list/"
        const val HOME_BANNER = BASE_URL + "banner" + RESPONSE_FORMAT

    }

}