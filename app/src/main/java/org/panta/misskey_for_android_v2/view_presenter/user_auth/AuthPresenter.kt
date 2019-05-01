package org.panta.misskey_for_android_v2.view_presenter.user_auth

import org.panta.misskey_for_android_v2.constant.getInstanceInfoList
import org.panta.misskey_for_android_v2.interfaces.AuthContract
import org.panta.misskey_for_android_v2.repository.AuthRepository
import org.panta.misskey_for_android_v2.repository.SecretRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator

class AuthPresenter(private val mView: AuthContract.View, private val sharedPref: SharedPreferenceOperator, domain: String?, appSecret: String?) : AuthContract.Presenter{

    private val secretRepository = SecretRepository(sharedPref)
    private val authRepository = if(domain == null || appSecret == null){
        val tmpDomain = sharedPref.getString("tmpDomain", null)
        val appSecretKey = getInstanceInfoList().first {
            it.domain == tmpDomain
        }.appSecret
        AuthRepository(domain = tmpDomain!!, appSecret = appSecretKey)
    }else{
        AuthRepository(domain = domain, appSecret = appSecret)
    }

    private var mDomain: String = domain ?: sharedPref.getString("tmpDomain", null)!!

    override fun getSession() {
        authRepository.getSession {
            sharedPref.put("tmpSession", it.token)
            sharedPref.put("tmpDomain", mDomain)
            mView.onLoadSession(it)
        }
    }

    override fun getUserToken() {
        val session = sharedPref.getString("tmpSession", null)
        val tmpDomain = sharedPref.getString("tmpDomain", null)
        if(session != null && tmpDomain != null){
            authRepository.getUserToken(session, {}){
                mView.onLoadUserToken(it, tmpDomain)
                //sharedPref.put(ApplicationConstant.APP_USER_TOKEN_KEY, it)
                //sharedPref.put(ApplicationConstant.APP_DOMAIN_KEY, tmpDomain)
                //authRepository.putUserToken(it)
                secretRepository.putUserToken(it)
                secretRepository.putDomain(tmpDomain)
            }
        }else{
            getSession()
        }
    }



    override fun start() {

    }
}