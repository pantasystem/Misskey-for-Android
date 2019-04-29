package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.SessionResponse
import org.panta.misskey_for_android_v2.entity.UserKeyResponse
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.util.sha256
import java.lang.StringBuilder
import java.net.URL
import java.security.MessageDigest

class AuthRepository(private val domain: String, private val appSecret: String){

    private val mapper = jacksonObjectMapper()
    private val connection = HttpsConnection()

    fun getSession(callBack: (SessionResponse)->Unit) = GlobalScope.launch {
        try{
            val map = HashMap<String, String>()
            map["appSecret"] = appSecret
            val json = mapper.writeValueAsString(map)
            Log.d("AuthRepository", "getSession json: $json")
            val stream = connection.post(URL("$domain/api/auth/session/generate"), json)
            val session: SessionResponse = mapper.readValue(stream)
            callBack(session)
        }catch(e: Exception){

        }

    }

    fun getUserToken(token: String, errorCallBack: (e: Exception)->Unit, callBack: (String) -> Unit) = GlobalScope.launch{
        try{

            val map = HashMap<String, String>()
            map["appSecret"] = appSecret
            map["token"] = token
            val json = mapper.writeValueAsString(map)
            Log.d("AuthRepository", "getAccessToken json: $json")

            val stream = connection.post(URL("$domain/api/auth/session/userkey"), json)
            val userKey : UserKeyResponse = mapper.readValue(stream)
            //val i = sha256("${userKey.accessToken}$appSecret")
            //Log.d("AuthRepository", "i: $i")
            callBack(userKey.accessToken)
        }catch(e: Exception){
            errorCallBack(e)
        }
    }


}