package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.net.URL
import java.util.*

abstract class AbsTimeline(private val timelineURL: URL, private val isDeployReplyTo: Boolean = false): IItemRepository<NoteViewData>{



    /*private var apiRequestCounter: Int = 0
        set(value){
            field = value
            GlobalScope.launch{
                delay(1000)
                field = 0
            }
        }*/

    private val mConnection = HttpsConnection()
    private val mapper = jacksonObjectMapper()
    private val converter = StreamConverter()

    private val noteAd = NoteAdjustment(isDeployReplyTo)


    //FIXME 直接Dataクラスを送信するようにする
    abstract fun createRequestTimelineJson(sinceId: String? = null, untilId: String? = null, sinceDate: Long? = null, untilDate: Long? = null): String

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{
      try{
            val jsonToRequest = createRequestTimelineJson(sinceId = sinceId)
            val list:List<Note> = reverseTimeline(requestTimeline(jsonToRequest))
            callBack(noteAd.insertReplyAndCreateInfo(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{

        try{
            val jsonToRequest = createRequestTimelineJson(untilId = untilId)

            val list:List<Note> = requestTimeline(jsonToRequest)
            callBack(noteAd.insertReplyAndCreateInfo(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }


    override fun getItems(callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{
        try{
            //FIXME 保存したタイムラインを読み取る処理を書く

            val cacheTimeline = emptyList<Note>()
            val timeline = if(cacheTimeline.isEmpty()){
                requestTimeline(createRequestTimelineJson(untilDate = Date().time))
            }else{
                cacheTimeline
            }

            callBack(noteAd.insertReplyAndCreateInfo(timeline))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }



    private fun requestTimeline(json: String): List<Note>{
        Log.d("AbsTimeline", "json $json")
        val receivedResult = converter.getString(mConnection.post(timelineURL, json))
        return mapper.readValue(receivedResult)
    }

    //FIXME　他のクラスでもよく使用するので分離予定
    private fun reverseTimeline(list: List<Note>):List<Note>{
        val reversedList = ArrayList<Note>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }







    private fun createReactionCountPair(reactionCount: Map<String, Int>?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }


}