package org.panta.misskey_for_android_v2.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_note.view.*
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener

class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    private var clickListener: NoteClickListener? = null
    private var userClickListener: UserClickListener? = null
    private var targetPrimaryId: String? = null
    private var note: Note? = null

    private val timelineItem = itemView.base_layout
    val whoReactionUserLink: Button = itemView.who_reaction_user_link
    private val userIcon: ImageView = itemView.user_icon
    private val userName: TextView = itemView.user_name
    private val userId: TextView = itemView.user_id
    private val noteText: TextView = itemView.note_text

    private val imageView1: ImageView = itemView.image_1
    private val imageView2: ImageView = itemView.image_2
    private val imageView3: ImageView = itemView.image_3
    private val imageView4: ImageView = itemView.image_4

    private val subUserIcon = itemView.sub_user_icon
    private val subUserName = itemView.sub_user_name
    private val subUserId = itemView.sub_user_id
    private val subNoteText: TextView = itemView.sub_text

    private val reactionView: GridView = itemView.reaction_view

    private val replyButton: ImageButton = itemView.reply_button
    private val replyCount: TextView = itemView.reply_count
    private val reNoteButton: ImageButton = itemView.re_note_button
    private val reNoteCount: TextView = itemView.re_note_count
    private val reactionButton: ImageButton = itemView.reaction_button
    private val descriptionButton: ImageButton = itemView.description_button

    init{
        viewInit()
        initButtonsClickListener()
    }

    fun viewInit(){
        whoReactionUserLink.visibility = View.GONE
        imageView1.visibility = View.GONE
        imageView2.visibility = View.GONE
        imageView3.visibility = View.GONE
        imageView4.visibility = View.GONE

        subUserIcon.visibility = View.GONE
        subUserName.visibility = View.GONE
        subUserId.visibility = View.GONE
        subNoteText.visibility =View.GONE
        reactionView.visibility = View.GONE

    }
    private fun initButtonsClickListener(){
        timelineItem.setOnClickListener {
            clickListener?.onNoteClicked(targetPrimaryId, note)
        }

        replyButton.setOnClickListener {
            clickListener?.onReplyButtonClicked(targetPrimaryId, note)

        }
        reNoteButton.setOnClickListener {
            clickListener?.onReNoteButtonClicked(targetPrimaryId, note)

        }
        reactionButton.setOnClickListener{
            clickListener?.onReactionButtonClicked(targetPrimaryId, note)
        }
        descriptionButton.setOnClickListener{
            clickListener?.onDescriptionButtonClicked(targetPrimaryId, note)
        }


    }


    fun setWhoReactionUserLink(user: User?, status: String){
        whoReactionUserLink.visibility = View.VISIBLE
        val text = "${user?.name}さんが${status}しました"
        whoReactionUserLink.text = text
        whoReactionUserLink.setOnClickListener{
            if(user != null){
                userClickListener?.onClickedUser(user)
            }
        }
    }

    fun setNoteText(text: String){
        noteText.text= text
    }

    fun setUser(user: User?){
        val listener = View.OnClickListener {
            when(it){
                userIcon, userName, userName ->{
                    if(user == null){

                    }else{
                        userClickListener?.onClickedUser(user)
                    }
                }
            }
        }
        userIcon.setOnClickListener(listener)
        userName.setOnClickListener(listener)
        userId.setOnClickListener(listener)

        setUserIcon(user?.avatarUrl?: "not found")
        setUserName(user?.name.toString())
        setUserId(user?.userName.toString())

    }
    fun setImage(urlList: List<String>){
        imageView1.visibility = View.GONE
        imageView2.visibility = View.GONE
        imageView3.visibility = View.GONE
        imageView4.visibility = View.GONE

        if(urlList.isNotEmpty()){
            imageView1.visibility = View.VISIBLE
            picasso(urlList[0], imageView1)
        }
        if(urlList.size >= 2){
            picasso(urlList[1], imageView2)
            imageView2.visibility = View.VISIBLE
        }

        if(urlList.size >= 3){
            picasso(urlList[2], imageView3)
            imageView3.visibility = View.VISIBLE
        }
        if(urlList.size >= 4){
            picasso(urlList[3], imageView4)
            imageView4.visibility = View.VISIBLE
        }


        val imageClickListener = View.OnClickListener { p0 ->
            val clickedImageIndex = when(p0){
                imageView1 -> 0
                imageView2 -> 1
                imageView3 -> 2
                imageView4 -> 3
                else -> 0
            }

            clickListener?.onImageClicked(clickedImageIndex, urlList.toTypedArray())
        }

        imageView1.setOnClickListener(imageClickListener)
        imageView2.setOnClickListener(imageClickListener)
        imageView3.setOnClickListener(imageClickListener)
        imageView4.setOnClickListener(imageClickListener)


    }

    fun setSubUser(user: User){
        setSubUserIcon(user.avatarUrl.toString())
        setSubUserName(user.name.toString())
        setSubUserId(user.userName)
    }

    private fun setSubUserIcon(imageUrl: String){
        subUserIcon.visibility = View.VISIBLE
        Picasso
            .get()
            .load(imageUrl)
            .into(subUserIcon)

    }

    private fun setSubUserName(userName: String){
        subUserName.text= userName
        subUserName.visibility = View.VISIBLE
    }

    private fun setSubUserId(userId: String){
        subUserId.text = userId
        subUserId.visibility = View.VISIBLE
    }

    fun setSubNoteText(text: String){
        subNoteText.text = text
        subNoteText.visibility = View.VISIBLE
    }



    fun setReplyCount(count: Int, isDigitTruncation:Boolean = true){
        if(count > 0){
            replyCount.text = count.toString()
            replyCount.visibility = View.VISIBLE
        }else{
            replyCount.visibility = View.INVISIBLE
        }
    }

    fun setReNoteCount(count: Int){
        if(count > 0){
            reNoteCount.text = count.toString()
            reNoteCount.visibility = View.VISIBLE
        }else{
            reNoteCount.visibility = View.INVISIBLE
        }
    }

    fun setReactionCount(adapter: ReactionCountAdapter){
        reactionView.adapter = adapter
        reactionView.visibility = View.VISIBLE
    }

    fun backgroundColor(code: Int){
        if(code == 1){
            timelineItem.setBackgroundColor(Color.parseColor("#d3d3d3"))
        }else{
            timelineItem.setBackgroundColor(Color.WHITE/*Color.parseColor("#fff3f3f3")*/)
        }
    }

    fun addOnItemClickListener(targetPrimaryId: String, note: Note, listener: NoteClickListener?){
        clickListener = listener
        this.targetPrimaryId = targetPrimaryId
        this.note = note

    }

    fun addOnUserClickListener(listener: UserClickListener){
        this.userClickListener = listener
    }

    private fun setUserIcon(imageUrl: String){
        Picasso
            .get()
            .load(imageUrl)
            .into(userIcon)
    }
    private fun setUserName(userNameText: String){
        userName.text = userNameText
    }
    private fun setUserId(userId: String){
        this.userId.text = userId
    }




    private fun picasso(imageUrl: String, imageView: ImageView){
        imageView.visibility = View.VISIBLE
        Picasso
            .get()
            .load(imageUrl)
            .into(imageView)
    }


}