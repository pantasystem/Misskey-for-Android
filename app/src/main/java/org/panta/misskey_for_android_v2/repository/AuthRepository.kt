package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.SessionResponse
import org.panta.misskey_for_android_v2.entity.UserKeyResponse
import org.panta.misskey_for_android_v2.network.HttpsConnection
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
    fun getAccessToken(token: String, errorCallBack: (e: Exception)->Unit, callBack: (String) -> Unit) = GlobalScope.launch{
        try{

            val map = HashMap<String, String>()
            map["appSecret"] = appSecret
            map["token"] = token
            val json = mapper.writeValueAsString(map)
            Log.d("AuthRepository", "getAccessToken json: $json")

            val stream = connection.post(URL("$domain/api/auth/session/userkey"), json)
            val userKey : UserKeyResponse = mapper.readValue(stream)
            val i = sha256("${userKey.accessToken}$appSecret")
            Log.d("AuthRepository", "i: $i")
            callBack(i.toString())
        }catch(e: Exception){
            errorCallBack(e)
        }
    }

    private fun sha256(input: String) = hashString("SHA-256", input)

    private fun hashString(type: String, input: String): String {
        val HEX_CHARS = "0123456789ABCDEF"
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }
        return result.toString()
    }
}