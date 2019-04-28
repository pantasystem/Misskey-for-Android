package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.NotificationProperty
import org.panta.misskey_for_android_v2.entity.RequestNotificationProperty
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import org.panta.misskey_for_android_v2.view_data.NotificationViewData
import java.net.URL

class Notification(private val domain: String, private val authKey: String): IItemRepository<NotificationViewData>{

    private val connection = HttpsConnection()
    private val mapper = jacksonObjectMapper()
    private val noteAd = NoteAdjustment()

    /*fun getNotification(sinceId: String? = null, untilId: String? = null,callBack: (List<NotificationViewData>)->Unit){
        GlobalScope.launch{
            try{
                val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
                val reqJson = mapper.writeValueAsString(reqObj)
                val responseStream = connection.post(URL("$domain/api/i/notifications"), reqJson)
                val resJson = StreamConverter().getString(responseStream)
                val resObj: List<NotificationProperty> = mapper.readValue(resJson)

                val data = makeViewData(resObj)
                callBack(data)
            }catch(e: Exception){
                Log.w("Notification", "getNotificationでエラー発生", e)
            }
        }
    }*/

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NotificationViewData>?) -> Unit)= GlobalScope.launch {
        try{
            val reqObj = RequestNotificationProperty(i = authKey, sinceId = sinceId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)

            val data = getNotificationData(reqJson)
            callBack(reverseList(data))
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
        }

    }
    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NotificationViewData>?) -> Unit) = GlobalScope.launch {
        try{
            val reqObj = RequestNotificationProperty(i = authKey, untilId = untilId ,limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            callBack(getNotificationData(reqJson))
        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
        }
    }
    override fun getItems(callBack: (timeline: List<NotificationViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val reqObj = RequestNotificationProperty(i = authKey, limit = 10)
            val reqJson = mapper.writeValueAsString(reqObj)
            callBack(getNotificationData(reqJson))

        }catch(e: Exception){
            Log.w("Notification", "getItemsでエラー発生", e)
        }


    }

    private suspend fun getNotificationData(json: String): List<NotificationViewData>{
        val responseStream = connection.post(URL("$domain/api/i/notifications"), json)
        val resJson = StreamConverter().getString(responseStream)
        val resObj: List<NotificationProperty> = mapper.readValue(resJson)

        return makeViewData(resObj)

    }

    private fun makeViewData(list: List<NotificationProperty>): List<NotificationViewData>{
        return list.map{
            if(it.note == null){
                NotificationViewData(it.id,false, it, null)
            }else{
                val viewData = NoteViewData(it.note.id, false,it.note, noteAd.checkUpNoteType(it.note), noteAd.createReactionCountPair(it.note.reactionCounts))
                NotificationViewData(it.id, false, it, viewData)
            }
        }
    }

    private fun reverseList(list: List<NotificationViewData>): List<NotificationViewData>{
        val arrayList = ArrayList<NotificationViewData>()
        for(i in (list.size - 1).downTo(0)){
            arrayList.add(list[i])
        }
        return arrayList
    }


}