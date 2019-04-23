package org.panta.misskey_for_android_v2.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.NotificationType
import org.panta.misskey_for_android_v2.entity.NotificationProperty

class NotificationAdapter(private val notificationList: List<NotificationProperty>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun getItemCount(): Int {
        return notificationList.size
    }

    override fun getItemViewType(position: Int): Int {
        return NotificationType.getEnumFromString(notificationList[position].type).ordinal
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

        /*return when(notificationType){
            NotificationType.FOLLOW, NotificationType.RENOTE, NotificationType.REACTION ->{
                Log.d("NotificationAdapter", "onCreateViewHolder params:$notificationType")
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_notification, p0,false)
                NotificationViewHolder(view)
            }
            else ->{
                //Log.d("NotificationAdapter", "onCreateViewHolder params:$notificationType")
                val view = LayoutInflater.from(p0.context).inflate(R.layout.item_note, p0, false)
                NoteViewHolder(view)
            }
        }*/


    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val data = notificationList[p1]
        if(p0 is NotificationViewHolder){
            p0.setNotification(data)
        }else if(p0 is NoteViewHolder){
            //p0.setNote(data)
        }
    }

}