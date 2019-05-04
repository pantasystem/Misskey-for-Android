package org.panta.misskey_for_android_v2.network

import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.net.URL

class OkHttpConnection{
    fun postString(url: URL, value: String): String?{
        return try{
            val client = OkHttpClient()
            val mediaType = MediaType.parse("application/json; charset=utf-8")
            val requestBody = RequestBody.create(mediaType, value)
            val request = Request.Builder().url(url).post(requestBody).build()
            val response = client.newCall(request).execute()
            response.body()?.string()
        }catch(e: Exception){
            Log.w("OkHttpConnection", "error", e)
            null
        }

    }

}