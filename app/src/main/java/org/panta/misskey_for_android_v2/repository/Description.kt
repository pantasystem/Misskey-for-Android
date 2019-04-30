package org.panta.misskey_for_android_v2.repository

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.usecase.NoteAdjustment
import org.panta.misskey_for_android_v2.view_data.NoteViewData

//FIXME そもそもDESCRIPTIONは別のActivity、別のFragmentで定義する可能性があるのでITimelineという型に
// こだわる必要性がない可能性
//TimelineAdapterのみ再利用する
class Description(){

    //過去ほど上になり現在に近いほど下になる通常のタイムラインとは真逆


    fun getNotesReply(noteViewData: NoteViewData, callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{

    }

    //ListをReverseする必要がある
    fun getOriginNotes(note: Note, callBack: (timeline: List<NoteViewData>?) -> Unit) = GlobalScope.launch{
        try{
            var reply: Note? = note.reply
            val replyList = ArrayList<NoteViewData>()
            replyList.add(NoteViewData(id = note.id, isIgnore = false ,note = note, type = NoteAdjustment.NoteType.NOTE, reactionCountPairList = ReactionCountPair.createList(note.reactionCounts!!)))
            while(reply != null){
                val reactionPair = if(reply.reactionCounts == null){
                    emptyList()
                }else{
                    //ReactionCountPair.createList(reply.reactionCounts!!)
                    reply.reactionCounts!!.map{
                        ReactionCountPair(it.key, it.value.toString())
                    }
                }
                replyList.add(NoteViewData(id = reply.id, isIgnore = false ,note = reply, type = NoteAdjustment.NoteType.NOTE, reactionCountPairList = reactionPair))
                reply = reply.reply
            }
            callBack(reverseTimeline(replyList))
        }catch(e: Exception){
            Log.w("Description", "getOriginNotesでエラー発生", e)
        }

    }

    private fun reverseTimeline(list: List<NoteViewData>):List<NoteViewData>{
        val reversedList = java.util.ArrayList<NoteViewData>()
        for(n in list.size - 1 downTo 0){
            reversedList.add(list[n])
        }
        return reversedList
    }
}