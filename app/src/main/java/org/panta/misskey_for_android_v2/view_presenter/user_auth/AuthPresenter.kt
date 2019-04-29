package org.panta.misskey_for_android_v2.view_presenter.user_auth

import android.net.Uri
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.interfaces.AuthContract
import org.panta.misskey_for_android_v2.repository.AuthRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import java.net.URI

class AuthPresenter(private val mView: AuthContract.View, private val domain: String, appSecret: String, private val sharedPref: SharedPreferenceOperator) : AuthContract.Presenter{

    private val authRepository = AuthRepository(domain = domain, appSecret = appSecret)


    override fun getSession() {
        authRepository.getSession {
            sharedPref.put("tmpSession", it.token)
            mView.onLoadSession(it)
        }
    }

    override fun getUserToken() {
        val session = sharedPref.get("tmpSession", null)
        if(session != null){
            authRepository.getUserToken(session, {}){
                mView.onLoadUserToken(it)
                sharedPref.put(ApplicationConstant.APP_USER_TOKEN_KEY, it)
                sharedPref.put(ApplicationConstant.APP_DOMAIN_KEY, domain)
            }
        }else{
            getSession()
        }
    }



    override fun start() {

    }
}