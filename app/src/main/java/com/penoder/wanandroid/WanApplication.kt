package com.penoder.wanandroid

import android.app.Application


/**
 * @author Penoder
 * @date 2018/8/19
 */
class WanApplication : Application() {

    companion object {
        val mInstance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            WanApplication()
        }
    }

}