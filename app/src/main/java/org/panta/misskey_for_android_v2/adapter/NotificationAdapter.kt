package org.panta.misskey_for_android_v2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.NotificationType
import org.panta.misskey_for_android_v2.repository.NoteAdjustment
import org.panta.misskey_for_android_v2.view_data.NotificationViewData

class NotificationAdapter(private val notificationList: List<NotificationViewData>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return NotificationType.getEnumFromString(notificationList[position].notificationProperty.type).ordinal
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {



        val isNotification = viewType == NotificationType.FOLLOW.ordinal|| viewType ==  NotificationType.RENOTE.ordinal || viewType == NotificationType.REACTION.ordinal
        return if(isNotification){
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_notification, p0,false)
            NotificationViewHolder(view)
        }else{
            //Log.d("NotificationAdapter", "onCreateViewHolder params:$notificationType")
            val view = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
            NoteViewHolder(view)
        }


    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, p1: Int) {
        val data = notificationList[p1]
        if(viewHolder is NotificationViewHolder){
            viewHolder.setNotification(data.notificationProperty)
        }else if(viewHolder is NoteViewHolder){
            val viewData = data.noteViewData
            viewHolder.invisibleReactionCount()
            when{
                viewData!!.type == NoteAdjustment.NoteType.REPLY -> {
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
            viewHolder.setNote(data.noteViewData!!)
        }
    }

}