package com.penoder.mylibrary.okhttp

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.penoder.mylibrary.utils.LogUtil
import com.penoder.mylibrary.utils.ToastUtil
import okhttp3.*
import java.io.IOException
import java.lang.reflect.Type

/**
 * @author asus
 * @date 2018/11/16
 */
class HttpClient(context: Context?) {

    private val mHeaderKey = "Android"
    private var mHeaderValue: String = ""
    private var mUrl: String = ""
    private var mIsPost: Boolean = false

    private var mContext: Context? = context

    private var mGson: Gson = GsonBuilder().create()
    private var mHandler: Handler = Handler(Looper.getMainLooper())

    private val paramsMap = HashMap<String, Any>()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: HttpClient? = null

        fun create(context: Context?): HttpClient {
            if (mInstance == null) {
                mInstance = HttpClient(context)
            }
            return mInstance as HttpClient
        }
    }

    fun addHeader(value: String): HttpClient {
        this.mHeaderValue = value
        return this
    }

    fun addParams(key: String, value: Any?): HttpClient {
        paramsMap[key] = value ?: ""
        return this
    }

    fun addUrl(url: String): HttpClient {
        this.mUrl = url
        return this
    }

    fun post(): HttpClient {
        mIsPost = true
        return this
    }

    fun <T>execute(listener: OkCallBack<T>, clazz: Class<T>) {
        val client = OkHttpClient()
        val requestBuilder = if (mIsPost) {
            val bodyBuilder = FormBody.Builder()
            for ((key, value) in paramsMap) {
                bodyBuilder.add(key, value.toString())
            }
            Request.Builder().url(mUrl).post(bodyBuilder.build())
        } else {
            val params = StringBuilder()
            for ((key, value) in paramsMap) {
                params.append("$key=$value&")
            }
            if (params.isNotEmpty()) {
                mUrl = "$mUrl?$params"
                mUrl = mUrl.substring(0, mUrl.length - 1)
            }
            Request.Builder().url(mUrl)
        }
        val request = requestBuilder.addHeader(mHeaderKey, mHeaderValue).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                mHandler.post {
                    listener.onResponse(false, null)
                }
                release()
            }

            override fun onResponse(call: Call?, response: Response?) {
                if (response == null) {
                    mHandler.post {
                        listener.onResponse(false, null)
                    }
                } else {
                    val jsonStr: String = response.body()?.string() ?: ""
                    val obj: T? = fromJson(jsonStr, clazz)
                    mHandler.post {
                        listener.onResponse(obj != null, obj)
                    }
                }
                release()
            }
        })
    }

    fun <T>execute(listener: OkCallBack<T>, type: Type) {
        val client = OkHttpClient()
        val requestBuilder = if (mIsPost) {
            val bodyBuilder = FormBody.Builder()
            for ((key, value) in paramsMap) {
                bodyBuilder.add(key, value.toString())
            }
            Request.Builder().url(mUrl).post(bodyBuilder.build())
        } else {
            val params = StringBuilder()
            for ((key, value) in paramsMap) {
                params.append("$key=$value&")
            }
            if (params.isNotEmpty()) {
                mUrl = "$mUrl?$params"
                mUrl = mUrl.substring(0, mUrl.length - 1)
            }
            Request.Builder().url(mUrl)
        }
        val request = requestBuilder.addHeader(mHeaderKey, mHeaderValue).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                release()
                mHandler.post {
                    listener.onResponse(false, null)
                }
            }

            override fun onResponse(call: Call?, response: Response?) {
                release()
                if (response == null) {
                    mHandler.post {
                        listener.onResponse(false, null)
                    }
                } else {
                    val jsonStr: String = response.body()?.string() ?: ""
                    val obj: T? = fromJson(jsonStr, type)
                    mHandler.post {
                        listener.onResponse(obj != null, obj)
                    }
                }
            }
        })
    }

    fun <T> fromJson(jsonStr: String? = "", clz: Class<T>): T? {
        val common: CommonJson<T>? = mGson.fromJson(jsonStr, CommonJson<T>()::class.java)
        if (common != null) {
            if (common.errorCode == 0) {
                val json: String? = mGson.toJson(common.data)
                LogUtil.i("HttpClient 响应结果： $json")
                return mGson.fromJson<T>(json, clz)
            } else {
                ToastUtil.showShortToast(mContext, common.errorMsg)
            }
        }
        return null
    }

    fun <T> fromJson(jsonStr: String? = "", type: Type): T? {
        val common: CommonJson<T>? = mGson.fromJson(jsonStr, CommonJson<T>()::class.java)
        if (common != null) {
            if (common.errorCode == 0) {
                val json: String? = mGson.toJson(common.data)
                LogUtil.i("HttpClient 响应结果： $json")
                return mGson.fromJson<T>(json, type)
            } else {
                ToastUtil.showShortToast(mContext, common.errorMsg)
            }
        }
        return null
    }

    private fun release() {
        paramsMap.clear()
        mHeaderValue = ""
        mUrl = ""
        mIsPost = false
    }

}