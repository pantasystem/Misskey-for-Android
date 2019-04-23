package org.panta.misskey_for_android_v2.adapter

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener
import org.panta.misskey_for_android_v2.repository.AbsTimeline
import org.panta.misskey_for_android_v2.repository.NoteAdjustment
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

    override fun onBindViewHolder(viewHolder: NoteViewHolder, p1: Int) {
        val viewData = notesList[p1]

       //リアクションをセットしている
        if(viewData.reactionCountPairList.isNotEmpty()){
            viewHolder.setReactionCount(ReactionCountAdapter(context, R.layout.item_reaction_counter, viewData.reactionCountPairList))
        }else{
            viewHolder.invisibleReactionCount()
        }
        when{
            viewData.type == NoteAdjustment.NoteType.REPLY -> {
                viewHolder.setReply(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.REPLY_TO ->{
                viewHolder.setReplyTo(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.NOTE -> {
                //これはNote
                viewHolder.setNote(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.RE_NOTE -> {
                //これはリノート
                viewHolder.setReNote(viewData)
            }
            viewData.type == NoteAdjustment.NoteType.QUOTE_RE_NOTE -> {
                viewHolder.setQuoteReNote(viewData)
            }
        }

        viewHolder.addOnItemClickListener(noteClickListener)
        viewHolder.addOnUserClickListener(userClickListener)
       
    }

    fun addAllFirst(list: List<NoteViewData>){
        if(notesList is ArrayList){
            synchronized(notesList){
                notesList.addAll(0, list)
            }
            Handler().post{
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