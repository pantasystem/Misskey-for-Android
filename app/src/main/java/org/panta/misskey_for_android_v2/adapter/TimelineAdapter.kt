package org.panta.misskey_for_android_v2.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.entity.FileProperty
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.repository.AbsTimeline
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelineAdapter(private val context: Context, private val notesList: List<NoteViewData>) : RecyclerView.Adapter<NoteViewHolder>(){

    var noteClickListener: NoteClickListener? = null

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NoteViewHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
        return NoteViewHolder(inflater)

    }

    override fun onBindViewHolder(p0: NoteViewHolder, p1: Int) {
        val viewData = notesList[p1]
        val note = notesList[p1].note

        Log.d("TimelineAdapter", "ReactionCount ${note.reactionCounts}")

        when {
            viewData.isOriginReply -> p0.backgroundColor(1)
            viewData.isReply -> {
                p0.setWhoReactionUserLink(note.user?.name?:"not found", "クソリプ", note.user?.id?:"not found")
                p0.backgroundColor(0)
            }
            else -> p0.backgroundColor(0)
        }

        if(viewData.reactionCountPairList.isNotEmpty()){
            p0.setReactionCount(ReactionCountAdapter(context, R.layout.item_reaction_counter, viewData.reactionCountPairList))
        }


        //このセット方法はいろいろ面倒なのでリファクタリング(NoteViewHolderを)予定
        when {
            viewData.type == AbsTimeline.NoteType.REPLY -> {

                p0.setUserName(note.user?.name?:"not found")
                p0.setUserId(note.user?.userName?: "not found")
                p0.setUserIcon(note.user?.avatarUrl?: "not found")
                p0.setNoteText(note.text?: "not found")

                setImageData(p0, note)
                p0.setReplyCount(note.replyCount)
                p0.setReNoteCount(note.reNoteCount)
                p0.addOnItemClickListener(note.id, note, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.NOTE -> {
                //これはNote
                p0.whoReactionUserLink.visibility = View.GONE
                p0.setUserId(note.user?.userName?: "user id not found")
                p0.setUserName(note.user?.name?: "user name not found")
                p0.setUserIcon(note.user?.avatarUrl?: "not found")
                p0.setNoteText(note.text?:"not found")
                //resultReaction(it.id)
                setImageData(p0, note)
                p0.setReplyCount(note.replyCount)
                p0.setReNoteCount(note.reNoteCount)
                p0.addOnItemClickListener(note.id, note, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.RE_NOTE -> {
                //これはリノート

                p0.setWhoReactionUserLink(note.user?.name?:"nof found", "無断リノート", note.user?.id?:"note found")
                p0.setUserId(note.renote?.user?.userName?: "user id not found")
                p0.setUserName(note.renote?.user?.name?: "user name not found")
                p0.setUserIcon(note.renote?.user?.avatarUrl?: "not found")
                p0.setNoteText(note.renote?.text?:"")

                setImageData(p0, note.renote!!)
                //resultReaction(it.renote.id)
                p0.setReplyCount(note.renote.replyCount)
                p0.setReNoteCount(note.renote.reNoteCount)

                p0.addOnItemClickListener(note.renote.id, note.renote, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.QUOTE_RE_NOTE -> {
                p0.setUserId(note.user?.id?: "user id not found")
                p0.setUserName(note.user?.userName?: "user name not found")
                p0.setUserIcon(note.user?.avatarUrl?: "not found")
                p0.setNoteText(note.text?: "not found")

                p0.setSubUserId(note.renote?.user?.userName?: "user id not found")
                p0.setSubUserName(note.renote?.user?.name?: "user name not found")
                p0.setSubUserIcon(note.renote?.user?.avatarUrl?: "not found")
                p0.setSubNoteText(note.renote?.text?:"")
                setImageData(p0, note)

                p0.setReplyCount(note.renote?.replyCount?:0)
                p0.setReNoteCount(note.renote?.reNoteCount?:0)

                //resultReaction(it.id)
                p0.addOnItemClickListener(note.id, note, noteClickListener)
            }

        }
    }

    private fun setImageData(p0: NoteViewHolder, data: Note){
        val fileList = data.files?:return
        val nonNullFiles = ArrayList<FileProperty>()
        for(m in fileList){
            if(m != null){
                nonNullFiles.add(m)
            }
        }
        val imageData = nonNullFiles.filter{
            it.type != null && it.type.startsWith("image")
        }.map{ it.url }
        val nonNullUrlList: ArrayList<String> = ArrayList<String>()
        for(n in imageData){
            if(n != null){
                nonNullUrlList.add(n)
            }
        }
        p0.setImage(nonNullUrlList)
    }

    fun addAllFirst(list: List<NoteViewData>){
        if(notesList is ArrayList){
            synchronized(notesList){
                notesList.addAll(0, list)
            }
            Handler().post{
                //実験段階不具合の可能性有り
                notifyItemRangeInserted(0, list.size)
            }
        }
    }

    fun addAllLast(list: List<NoteViewData>){
        if(notesList is ArrayList){
            val lastIndex = notesList.size
            synchronized(notesList){
                notesList.addAll(list)
            }
            Handler().post{
                //実験段階不具合の可能性有り
                notifyItemRangeInserted(lastIndex, list.size)
            }
        }
    }

    fun addNoteClickListener(listener: NoteClickListener){
        this.noteClickListener = listener
    }



}