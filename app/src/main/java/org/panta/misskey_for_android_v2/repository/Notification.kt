package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.NotificationProperty
import org.panta.misskey_for_android_v2.entity.RequestNotificationProperty
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import java.net.URL

class Notification(private val domain: String, private val authKey: String){

    private val connection = HttpsConnection()
    private val mapper = jacksonObjectMapper()

    fun getNotification(sinceId: String? = null, untilId: String? = null,callBack: (List<NotificationProperty>)->Unit){
        GlobalScope.launch{
            try{
                val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
                val reqJson = mapper.writeValueAsString(reqObj)
                val responseStream = connection.post(URL("$domain/api/i/notification"), reqJson)
                val resJson = StreamConverter().getString(responseStream)
                val resObj: List<NotificationProperty> = mapper.readValue(resJson)
                callBack(resObj)
            }catch(e: Exception){
                Log.w("Notification", "getNotificationでエラー発生", e)
            }
        }
    }


}