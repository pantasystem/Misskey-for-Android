package org.panta.misskey_for_android_v2.view_presenter.user_auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.panta.misskey_for_android_v2.R

const val AUTH_WEB_VIEW_URL_TAG = "AUTH_WEB_VIEW_ACTIVITY_URL_TAG"
class AuthWebViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_web_view)

        val intent = Intent()
        val url: String? = intent.getStringExtra(AUTH_WEB_VIEW_URL_TAG)

    }
}
