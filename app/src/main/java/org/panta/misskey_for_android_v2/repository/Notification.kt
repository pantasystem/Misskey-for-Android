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
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import org.panta.misskey_for_android_v2.view_data.NotificationViewData
import java.net.URL

class Notification(private val domain: String, private val authKey: String){

    private val connection = HttpsConnection()
    private val mapper = jacksonObjectMapper()
    private val noteAd = NoteAdjustment()

    fun getNotification(sinceId: String? = null, untilId: String? = null,callBack: (List<NotificationViewData>)->Unit){
        GlobalScope.launch{
            try{
                val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
                val reqJson = mapper.writeValueAsString(reqObj)
                val responseStream = connection.post(URL("$domain/api/i/notifications"), reqJson)
                val resJson = StreamConverter().getString(responseStream)
                val resObj: List<NotificationProperty> = mapper.readValue(resJson)

                val data = resObj.map{
                    if(it.note == null){
                        NotificationViewData(it, null)
                    }else{
                        val viewData = NoteViewData(it.note, noteAd.checkUpNoteType(it.note), noteAd.createReactionCountPair(it.note.reactionCounts))
                        NotificationViewData(it, viewData)
                    }
                }
                callBack(data)
            }catch(e: Exception){
                Log.w("Notification", "getNotificationでエラー発生", e)
            }
        }
    }


}