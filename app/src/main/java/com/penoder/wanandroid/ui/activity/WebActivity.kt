package com.penoder.wanandroid.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.penoder.mylibrary.utils.LogUtil
import com.penoder.wanandroid.R
import com.penoder.wanandroid.databinding.ActivityWebBinding
import com.penoder.wanandroid.ui.base.BaseActivity

class WebActivity : BaseActivity<ActivityWebBinding>() {

    companion object {
        private const val LOAD_URL: String = "LOAD_URL"

        fun startSelf(context: Context?, url: String?) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra(LOAD_URL, url)
            context?.startActivity(intent)
        }
    }

    override fun getLayoutID(): Int = R.layout.activity_web

    @SuppressLint("SetJavaScriptEnabled")
    override fun initialView() {
        super.initialView()
        val webSetting = getBinding()?.webView?.settings
        webSetting?.javaScriptEnabled = true
        webSetting?.useWideViewPort = true
        webSetting?.loadWithOverviewMode = true
        webSetting?.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSetting?.domStorageEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSetting?.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        getBinding()?.webView?.setOnLongClickListener { true }
        getBinding()?.webView?.requestFocus()
        getBinding()?.webView?.webViewClient = MyWebViewClient()
        getBinding()?.webView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                getBinding()?.progressWebView?.progress = view?.progress!!
                if (getBinding()?.progressWebView?.progress!! > 88) {
                    getBinding()?.progressWebView?.visibility = View.GONE
                }
            }
        }
        loadUrl(intent.getStringExtra(LOAD_URL))
    }

    private fun loadUrl(url: String?) {
        getBinding()?.webView?.loadUrl(url)
    }

    override fun onBackPressed() {
        if (getBinding()?.webView?.canGoBack()!!) {
            getBinding()?.webView?.goBack()
        } else {
            super.onBackPressed()
        }
    }

    inner class MyWebViewClient : WebViewClient() {
        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            LogUtil.i("点击的超链接： $url")
            loadUrl(url)
            return false
        }
    }

}