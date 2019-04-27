package org.panta.misskey_for_android_v2.view_presenter.user_auth

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_auth_web_view.*
import org.panta.misskey_for_android_v2.R

const val AUTH_WEB_VIEW_URL_TAG = "AUTH_WEB_VIEW_ACTIVITY_URL_TAG"
class AuthWebViewActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_web_view)

        val intent = Intent()
        val url: String? = intent.getStringExtra(AUTH_WEB_VIEW_URL_TAG)

        auth_web_view.webViewClient = WebViewClient()
        auth_web_view.isEnabled
        val setting = auth_web_view.settings
        setting.javaScriptEnabled = true
        auth_web_view.loadUrl(url)
    }
}
