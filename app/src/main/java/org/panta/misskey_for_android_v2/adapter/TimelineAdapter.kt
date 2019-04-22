package org.panta.misskey_for_android_v2.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ReactionConstData
import org.panta.misskey_for_android_v2.entity.FileProperty
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener
import org.panta.misskey_for_android_v2.repository.AbsTimeline
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelineAdapter(private val context: Context, private val notesList: List<NoteViewData>) : RecyclerView.Adapter<NoteViewHolder>(){

    private var noteClickListener: NoteClickListener? = null
    private var userClickListener: UserClickListener? = null

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

        p0.viewInit()

        when {
            viewData.isOriginReply -> p0.backgroundColor(1)
            viewData.isReply -> {
                p0.setWhoReactionUserLink(note.user, "クソリプ")
                p0.backgroundColor(0)
            }
            else -> p0.backgroundColor(0)
        }

        if(viewData.reactionCountPairList.isNotEmpty()){
            p0.setReactionCount(ReactionCountAdapter(context, R.layout.item_reaction_counter, viewData.reactionCountPairList))
        }
        //Test
        /*val testReactionData = arrayListOf(
            ReactionCountPair(ReactionConstData.CONGRATS, "10"),
            ReactionCountPair(ReactionConstData.CONFUSED, "10"),
            ReactionCountPair(ReactionConstData.ANGRY, "10"),
            ReactionCountPair(ReactionConstData.HMM, "10"),
            ReactionCountPair(ReactionConstData.LAUGH, "10"),
            ReactionCountPair(ReactionConstData.LIKE, "10"),
            ReactionCountPair(ReactionConstData.LOVE, "10"),
            ReactionCountPair(ReactionConstData.PUDDING, "10"),
            ReactionCountPair(ReactionConstData.RIP, "10"),
            ReactionCountPair(ReactionConstData.SURPRISE, "10")
            )
        p0.setReactionCount(ReactionCountAdapter(context, R.layout.item_reaction_counter, testReactionData))*/


        if(userClickListener != null){
            p0.addOnUserClickListener(userClickListener!!)
        }
        //このセット方法はいろいろ面倒なのでリファクタリング(NoteViewHolderを)予定
        when {
            viewData.type == AbsTimeline.NoteType.REPLY -> {
                setImageData(p0, note)

                p0.setNote(note)
                p0.addOnItemClickListener(note.id, note, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.NOTE -> {
                //これはNote
                p0.whoReactionUserLink.visibility = View.GONE
                setImageData(p0, note)

                p0.setNote(note)
                p0.addOnItemClickListener(note.id, note, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.RE_NOTE -> {
                //これはリノート
                setImageData(p0, note.renote!!)

                p0.setWhoReactionUserLink(note.user, "無断リノート")
                p0.setNote(note.renote)

                p0.addOnItemClickListener(note.renote.id, note.renote, noteClickListener)
            }
            viewData.type == AbsTimeline.NoteType.QUOTE_RE_NOTE -> {



                if(note.renote?.user != null){
                    p0.setSubUser(note.renote.user)
                }

                p0.setSubNoteText(note.renote?.text?:"")
                setImageData(p0, note)

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
        }
        val nonNullUrlList = ArrayList<FileProperty>()
        for(n in imageData){
            if(n.url != null){
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

    fun addUserClickListener(listener: UserClickListener){
        this.userClickListener = listener
    }



}