package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.interfaces.CallBackListener
import org.panta.misskey_for_android_v2.interfaces.ITimeline
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.net.URL
import java.util.*

abstract class AbsTimeline(private val timelineURL: URL): ITimeline{

    enum class NoteType{
        REPLY,
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

    override fun getNotesUseSinceId(noteId: String, callBack: (timeline: List<NoteViewData>)->Unit) = GlobalScope.launch{

        if(apiRequestCounter > 0){
            return@launch
        }

        try{
            apiRequestCounter++
            val jsonToRequest = createRequestTimelineJson(sinceId = noteId)
            val list:List<Note> = reverseTimeline(requestTimeline(jsonToRequest))
            callBack(insertReply(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }

    override fun getNotesUseUntilId(noteId: String, callBack: (timeline: List<NoteViewData>)->Unit) = GlobalScope.launch{
        if(apiRequestCounter > 0){
            return@launch
        }
        try{
            apiRequestCounter++
            val jsonToRequest = createRequestTimelineJson(untilId = noteId)

            val list:List<Note> = requestTimeline(jsonToRequest)
            callBack(insertReply(list))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }


    override fun getTimeline(callBack: (timeline: List<NoteViewData>) -> Unit) = GlobalScope.launch{
        if(apiRequestCounter > 0){
            return@launch
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

            callBack(insertReply(timeline))
        }catch(e: Exception){
            Log.e("AbsTimeline", "エラー発生",e)
        }

    }

    //分離予定
    override fun listenTimelineUpdate(callBack: CallBackListener<Note>): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeListenTimelineUpdate() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun requestTimeline(json: String): List<Note>{
        Log.d("AbsTimeline", "json $json")
        val receivedResult = converter.getString(mConnection.post(timelineURL, json))
        return mapper.readValue(receivedResult)
    }

    override fun addListenNoteUpdate(noteId: String, callBack: CallBackListener<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun removeListenNoteUpdate(noteId: String, callBack: CallBackListener<String>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun reverseTimeline(list: List<Note>):List<Note>{
        val reversedList = ArrayList<Note>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }

    private fun insertReply(list: List<Note>): List<NoteViewData>{
        val replyList = ArrayList<NoteViewData>()
        for(n in list){
            val noteType = checkUpNoteType(n)
            val reply = n.reply
            if(reply == null){
                replyList.add(NoteViewData(n, isReply = false, isOriginReply = false, type = noteType))
            }else{
                replyList.add(NoteViewData(reply, isReply = false, isOriginReply = true, type = noteType))
                replyList.add(NoteViewData(n, isReply = true, isOriginReply = false, type = noteType))
            }
        }
        return replyList
    }

    private fun checkUpNoteType(note: Note): NoteType{
        return if(note.reply != null){
           //これはリプ
            NoteType.REPLY

        }else if(note.text != null && note.renote == null){
            //これはNote
            NoteType.NOTE
        }else if(note.renote != null && note.text == null){
            //これはリノート
            NoteType.RE_NOTE

        }else if(note.renote != null && note.text != null){
            //これは引用リノート
            NoteType.QUOTE_RE_NOTE
        }else{
            NoteType.DO_NOT_KNOW
        }
    }


}