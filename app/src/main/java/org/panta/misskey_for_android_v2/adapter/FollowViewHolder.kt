package org.panta.misskey_for_android_v2.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_follow_follower.view.*
import org.panta.misskey_for_android_v2.entity.FollowProperty
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.view_data.FollowViewData

class FollowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private val statusText: TextView = itemView.status_text
    private val userIcon: ImageView = itemView.user_icon
    private val userName: TextView = itemView.user_name
    private val userIdView: TextView = itemView.user_id
    private val descriptionView: TextView = itemView.user_description
    private val followUnFollowButton: Button = itemView.follow_un_follow_button

    fun setFollowing(data: User){
        setUserInfo(data)
        followUnFollowButton.text = "フォロー中"
    }

    fun setFollower(data: User){
        setUserInfo(data)
        followUnFollowButton.text = "どうすんの"
    }

    private fun setUserInfo(data: User){
        statusText.visibility = View.GONE
        setImage(userIcon, data.avatarUrl.toString())
        userName.text = data.name
        userIdView.text = data.userName
        descriptionView.text = data.description
    }

    private fun setImage(imageView: ImageView, url: String){
        Picasso
            .get()
            .load(url)
            .into(imageView)
    }
}