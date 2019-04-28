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
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.repository.AuthRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.view_presenter.DOMAIN_AUTH_KEY_TAG
import org.panta.misskey_for_android_v2.view_presenter.MainActivity

class AuthActivity : AppCompatActivity() {

    private val auth = AuthRepository(domain = ApplicationConstant.domain, appSecret = ApplicationConstant.appSecret)

    private var token: String? = null
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        val sp  = SharedPreferenceOperator(getSharedPreferences("tokenData", Context.MODE_PRIVATE))


        if(Intent.ACTION_VIEW == intent.action){
            Log.d("AuthActivity", "呼び出された")

            auth.getAccessToken( sp.get("token", null)!! , {
                Log.w("AuthActivity", "getAccessTokenしようとしたらエラー発生", it)
            }){
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.putExtra(DOMAIN_AUTH_KEY_TAG, DomainAuthKeyPair(domain = domain, i = it))
                startActivity(intent)
            }
        }else{
            auth.getSession {
                sp.put("token", it.token)
                this.token = it.token
                this.url = it.url
            }

            auth_button.setOnClickListener{
                if(token != null && url != null){

                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                }else{
                    Log.d("AuthActivity", "token && urlがNULL")
                }
            }
        }

    }

}
