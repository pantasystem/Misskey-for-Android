package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import java.net.URL

class MyInfo(val domain: String, private val authKey: String){
    private val httpsConnection = HttpsConnection()
    fun getMyInfo(callBack: (User)->Unit){
        GlobalScope.launch{
            try{
                val json = "{\"i\":\"$authKey\"}"
                val responseStream = httpsConnection.post(URL("$domain/api/i"), json)
                val responseJson = StreamConverter().getString(responseStream)
                Log.d("MyInfo", responseJson)
                val obj = jacksonObjectMapper().readValue<User>(responseJson)
                callBack(obj)
            }catch(e: Exception){
                Log.w("MyInfo", "getMyInfoでエラー発生", e)
            }
        }

    }
}