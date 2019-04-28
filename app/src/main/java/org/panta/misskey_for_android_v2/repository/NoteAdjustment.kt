package org.panta.misskey_for_android_v2.repository

import android.util.Log
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.util.ArrayList

class NoteAdjustment(private val isDeployReplyTo: Boolean = true){
    enum class NoteType{
        REPLY,
        REPLY_TO,   //返信先
        NOTE,
        RE_NOTE,
        QUOTE_RE_NOTE,
        DO_NOT_KNOW
    }
    fun insertReplyAndCreateInfo(list: List<Note>): List<NoteViewData>{
        val replyList = ArrayList<NoteViewData>()
        for(n in list){
            val noteType = checkUpNoteType(n)
            val reply = n.reply
            when(noteType){
                NoteType.NOTE, NoteType.QUOTE_RE_NOTE -> replyList.add(NoteViewData(n.id,false,n, type = noteType, reactionCountPairList = createReactionCountPair(n.reactionCounts)))
                NoteType.RE_NOTE -> replyList.add(NoteViewData(n.id, false, n, type = noteType, reactionCountPairList = createReactionCountPair(n.renote?.reactionCounts)))

                NoteType.REPLY ->{

                    if(isDeployReplyTo){
                        replyList.add(NoteViewData(reply!!.id, true,reply, type = NoteType.REPLY_TO, reactionCountPairList = createReactionCountPair(reply.reactionCounts)))
                    }
                    replyList.add(NoteViewData(n.id,false, n, type = NoteType.REPLY, reactionCountPairList = createReactionCountPair(n.reactionCounts)))
                }
                else-> {
                    Log.w("AbsTimeline", "わからないタイプのノートが来てしまった:$n")
                }
            }

        }
        return replyList
    }



    //FIXME メディアOnlyの時にうまく認識できない
    fun checkUpNoteType(note: Note): NoteType {
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

    fun createReactionCountPair(reactionCount: Map<String, Int>?): List<ReactionCountPair>{
        if(reactionCount == null){
            return emptyList()
        }
        return ReactionCountPair.createList(reactionCount)
    }
}