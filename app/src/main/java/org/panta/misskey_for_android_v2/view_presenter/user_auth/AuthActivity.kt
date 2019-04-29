package org.panta.misskey_for_android_v2.view_presenter.user_auth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_auth.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.domain
import org.panta.misskey_for_android_v2.constant.getAppSecretKey
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.SessionResponse
import org.panta.misskey_for_android_v2.interfaces.AuthContract
import org.panta.misskey_for_android_v2.repository.AuthRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.util.sha256
import org.panta.misskey_for_android_v2.view_presenter.DOMAIN_TAG
import org.panta.misskey_for_android_v2.view_presenter.MainActivity
import org.panta.misskey_for_android_v2.view_presenter.USER_TOKEN_TAG
import java.net.URI

class AuthActivity : AppCompatActivity(), AuthContract.View {

    override lateinit var mPresenter: AuthContract.Presenter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        //val sp  = SharedPreferenceOperator(getSharedPreferences("tokenData", Context.MODE_PRIVATE))

        val sharedPref = SharedPreferenceOperator(getSharedPreferences(ApplicationConstant.APP_SHARED_PREF_KEY, Context.MODE_PRIVATE))
        mPresenter = AuthPresenter(this, ApplicationConstant.domain, getAppSecretKey(), sharedPref)

        if(Intent.ACTION_VIEW == intent.action){
            Log.d("AuthActivity", "呼び出された")

            mPresenter.getUserToken()
        }

        auth_button.setOnClickListener{
            mPresenter.getSession()
        }

    }

    override fun onLoadSession(session: SessionResponse) {
        this.showBrowser(Uri.parse(session.url))
    }

    override fun onLoadUserToken(token: String) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra(USER_TOKEN_TAG, token)
        intent.putExtra(DOMAIN_TAG, domain)
        startActivity(intent)
    }

    override fun showBrowser(uri: Uri) {
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }



}
