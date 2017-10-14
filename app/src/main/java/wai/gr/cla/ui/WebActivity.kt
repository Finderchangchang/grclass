package wai.gr.cla.ui

import android.net.http.SslError
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_web.*

import wai.gr.cla.R
import wai.gr.cla.base.BaseActivity

/**
 * 加载web的内容
 * */
class WebActivity : BaseActivity() {
    override fun setLayout(): Int {
        return R.layout.activity_web
    }

    var title = ""
    var url = ""
    override fun initViews() {
        title = intent.getStringExtra("title")
        if (!TextUtils.isEmpty(title)) {
            toolbar.setCentertv(title)
        }
        toolbar.setLeftClick { finish() }
        url = intent.getStringExtra("url")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            web.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        web.loadUrl(url)
        val webSetting = web.settings
        webSetting.javaScriptEnabled = true
        webSetting.useWideViewPort = true;
        webSetting.loadWithOverviewMode = true;

        web.setWebViewClient(object : WebViewClient() {
            override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                handler.proceed()
            }
        })
    }

    override fun initEvents() {

    }

    override fun onPause() {
        web.destroy()
        super.onPause()
    }
}
