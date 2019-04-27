package org.panta.misskey_for_android_v2.view_presenter.user_auth

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_auth.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.repository.AuthRepository

const val ACTIVITY_RESULT_CODE = 9789
class AuthActivity : AppCompatActivity() {

    private val auth = AuthRepository(domain = ApplicationConstant.domain, appSecret = ApplicationConstant.appSeacret)

    private var token: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        auth.getSession {
            this.token = it.token
            this.url = it.url
        }

        auth_button.setOnClickListener{
            if(token != null && url != null){
                val intent = Intent(applicationContext, AuthWebViewActivity::class.java)
                intent.putExtra(AUTH_WEB_VIEW_URL_TAG, url.toString())
                startActivityForResult(intent, ACTIVITY_RESULT_CODE)
            }else{
                Log.d("AuthActivity", "token && urlがNULL")
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == ACTIVITY_RESULT_CODE){
            if(resultCode == RESULT_OK){

            }
        }
    }
}
