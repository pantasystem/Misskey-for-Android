package org.panta.misskey_for_android_v2.repository

import org.panta.misskey_for_android_v2.constant.getInstanceInfoList
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.util.sha256

class SecretRepository(private val sharedPreferenceOperator: ISharedPreferenceOperator){

    companion object{
        private const val APP_DOMAIN = "misskey_account_domain"
        private const val APP_USER_TOKEN = "misskey_account_user_token"
    }

    fun getDomain(): String?{
        return sharedPreferenceOperator.get(APP_DOMAIN, null)
    }

    fun getUserToken(): String?{
        return sharedPreferenceOperator.get(APP_USER_TOKEN, null)
    }

    fun getConnectionInfo(): ConnectionProperty?{
        val domain = getDomain()
        val userToken = getUserToken()
        val instanceInfoList = getInstanceInfoList()
        val appSecret = instanceInfoList.firstOrNull{
            it.domain == domain
        }
        return if(appSecret != null && domain != null){
            val i =  sha256("$userToken$appSecret")
            ConnectionProperty(domain, i)
        }else{
            null
        }
    }

    fun putDomain(token: String){
        sharedPreferenceOperator.put(APP_DOMAIN, token)
    }

    fun putUserToken(token: String){
        sharedPreferenceOperator.put(APP_USER_TOKEN, token)
    }
}