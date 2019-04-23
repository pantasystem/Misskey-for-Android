package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCount
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.interfaces.ITimeline
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.net.URL
import java.util.*

abstract class AbsTimeline(private val timelineURL: URL): ITimeline{

    enum class NoteType{
        REPLY,
        REPLY_TO,   //返信先
        NOTE,
        RE_NOTE,
        QUOTE_RE_NOTE,
        DO_NOT_KNOW
    }

    private var apiRequestCounter: Int = 0
        set(value){
            field = value
            GlobalScope.launch{
                delay(1000)
                field = 0
            }
        }

    private val mConnection = HttpsConnection()
    private val mapper = jacksonObjectMapper()
    private val converter = StreamConverter()


    //JSON作成関数　TimelineクラスはGlobal,Home,Localで分けることにしたのでここを実装して異なるパターンのJSONに対応する
    abstract fun createRequestTimelineJson(sinceId: String? = null, untilId: String? = null, sinceDate: Long? = null, untilDate: Long? = null): String

    override fun getNotesUseSinceId(noteViewData: NoteViewData, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{

        if(apiRequestCounter > 0){
            return@launch callBack(null)
        }

        try{
            apiRequestCounter++
            val jsonToRequest = createRequestTimelineJson(sinceId = noteViewData.note.id)
            val list:List<Note> = reverseTimeline(requestTimeline(jsonToRequest))
            callBack(insertReplyAndCreateInfo(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }

    override fun getNotesUseUntilId(noteViewData: NoteViewData, callBack: (timeline: List<NoteViewData>?)->Unit) = GlobalScope.launch{
        if(apiRequestCounter > 0){
            return@launch callBack(null)
        }
        try{
            apiRequestCounter++
            val jsonToRequest = createRequestTimelineJson(untilId = noteViewData.note.id)

            val list:List<Note> = requestTimeline(jsonToRequest)
            callBack(insertReplyAndCreateInfo(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }


    override fun getTimeline(callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{
        if(apiRequestCounter > 0){
            return@launch callBack(null)
        }
        try{
            //FIXME 保存したタイムラインを読み取る処理を書く

            apiRequestCounter++
            val cacheTimeline = emptyList<Note>()
            val timeline = if(cacheTimeline.isEmpty()){
                requestTimeline(createRequestTimelineJson(untilDate = Date().time))
            }else{
                cacheTimeline
            }

            callBack(insertReplyAndCreateInfo(timeline))
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

    private fun insertReplyAndCreateInfo(list: List<Note>): List<NoteViewData>{
        val replyList = ArrayList<NoteViewData>()
        for(n in list){
            val noteType = checkUpNoteType(n)
            val reply = n.reply
            when(noteType){
                NoteType.NOTE,NoteType.QUOTE_RE_NOTE -> replyList.add(NoteViewData(n, type = noteType, reactionCountPairList = createReactionCountPair(n.reactionCounts)))
                NoteType.RE_NOTE -> replyList.add(NoteViewData(n, type = noteType, reactionCountPairList = createReactionCountPair(n.renote?.reactionCounts)))

                NoteType.REPLY ->{

                    replyList.add(NoteViewData(reply!!, type = NoteType.REPLY_TO, reactionCountPairList = createReactionCountPair(reply.reactionCounts)))
                    replyList.add(NoteViewData(n, type = NoteType.REPLY, reactionCountPairList = createReactionCountPair(n.reactionCounts)))
                }
                else-> {
                    Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$n")
                }
            }

        }
        return replyList
    }



    //FIXME メディアOnlyの時にうまく認識できない
    private fun checkUpNoteType(note: Note): NoteType{
        return if(note.reply != null){
            //これはリプ
            NoteType.REPLY
        }else if(note.reNoteId == null && (note.text != null || note.files != null)){
            //これはNote
            NoteType.NOTE
        }else if(note.reNoteId != null && note.text == null && note.files.isNullOrEmpty()){
            //これはリノート
            NoteType.RE_NOTE

        }else if(note.reNoteId != null && (note.text != null || note.files != null)){
            //これは引用リノート
            NoteType.QUOTE_RE_NOTE
        }else{
            NoteType.DO_NOT_KNOW
        }
    }

    private fun createReactionCountPair(reactionCount: ReactionCount?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }


}