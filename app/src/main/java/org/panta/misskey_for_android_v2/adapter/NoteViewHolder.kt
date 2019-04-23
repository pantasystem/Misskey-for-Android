package org.panta.misskey_for_android_v2.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_note.view.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.entity.FileProperty
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.NoteClickListener
import org.panta.misskey_for_android_v2.interfaces.UserClickListener

open class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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
    private val imageViewList: List<ImageView>

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
        imageViewList = listOf(imageView1, imageView2, imageView3, imageView4)
        viewInit()

        initButtonsClickListener()

    }

    fun viewInit(){
        whoReactionUserLink.visibility = View.GONE
        imageViewList.forEach{
            it.visibility = View.GONE
        }

        subUserIcon.visibility = View.GONE
        subUserName.visibility = View.GONE
        subUserId.visibility = View.GONE
        subNoteText.visibility =View.GONE
        reactionView.visibility = View.GONE

    }

    fun setWhoReactionUserLink(user: User?, status: String){
        whoReactionUserLink.visibility = View.VISIBLE
        val text = "${user?.name?:user?.userName}さんが${status}しました"
        whoReactionUserLink.text = text
        whoReactionUserLink.setOnClickListener{
            if(user != null){
                userClickListener?.onClickedUser(user)
            }
        }
    }

    fun setNote(note: Note){
        this.setUser(note.user)
        if(note.text == null){
            noteText.visibility = View.GONE
        }else{
            noteText.visibility = View.VISIBLE
            noteText.text = note.text
        }
        //setImageData(p0, note)
        this.setImage(filterImageData(note))
        this.setReplyCount(note.replyCount)
        this.setReNoteCount(note.reNoteCount)
    }

    private fun setImage(fileList: List<FileProperty>){

        val imageClickListener = View.OnClickListener { p0 ->
            val clickedImageIndex = when(p0){
                imageView1 -> 0
                imageView2 -> 1
                imageView3 -> 2
                imageView4 -> 3
                else -> 0
            }

            val urlList: List<String> = fileList.map{it.url}.filter{it != null && it.isNotBlank()}.map{it.toString()}
            clickListener?.onImageClicked(clickedImageIndex, urlList.toTypedArray())
        }

        imageViewList.forEach{
            it.visibility = View.GONE
            it.setOnClickListener(imageClickListener)
        }


        for(n in 0.until(fileList.size)){
            picasso(fileList[n].url!!, imageViewList[n], fileList[n].isSensitive)
        }

    }

    fun setSubUser(user: User){
        subUserIcon.setOnClickListener {
            userClickListener?.onClickedUser(user)
        }
        subUserName.setOnClickListener{
            userClickListener?.onClickedUser(user)
        }
        subUserId.setOnClickListener {
            userClickListener?.onClickedUser(user)
        }
        setSubUserIcon(user.avatarUrl.toString())
        setSubUserName(user.name?: user.userName)
        setSubUserId(getUserId(user.userName, user.host))
    }

    fun setSubNoteText(text: String){
        subNoteText.text = text
        subNoteText.visibility = View.VISIBLE
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
    fun addOnUserClickListener(listener: UserClickListener?){
        this.userClickListener = listener
    }

    private fun setSubUserName(userName: String){
        subUserName.text= userName
        subUserName.visibility = View.VISIBLE
    }

    private fun setSubUserId(userId: String){
        subUserId.text = userId
        subUserId.visibility = View.VISIBLE
    }

    /*private fun setNoteText(text: String){
        noteText.text= text
    }*/

    private fun setUser(user: User?){
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
        this.userName.text = user?.name?: user?.userName.toString()
        this.userId.text = getUserId(user?.userName.toString(), user?.host)

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
    private fun setSubUserIcon(imageUrl: String){
        subUserIcon.visibility = View.VISIBLE
        Picasso
            .get()
            .load(imageUrl)
            .into(subUserIcon)

    }

    private fun getUserId(userId: String, host: String?): String{
        return if(host == null){
            "@$userId"
        }else{
            "@$userId@$host"
        }
    }

    private fun setReplyCount(count: Int, isDigitTruncation:Boolean = true){
        if(count > 0){
            replyCount.text = count.toString()
            replyCount.visibility = View.VISIBLE
        }else{
            replyCount.visibility = View.INVISIBLE
        }
    }

    private fun setReNoteCount(count: Int){
        if(count > 0){
            reNoteCount.text = count.toString()
            reNoteCount.visibility = View.VISIBLE
        }else{
            reNoteCount.visibility = View.INVISIBLE
        }
    }

    private fun setUserIcon(imageUrl: String){
        Picasso
            .get()
            .load(imageUrl)
            .into(userIcon)
    }


    private fun filterImageData(data: Note): List<FileProperty>{

        val fileList = data.files ?: return emptyList()
        val nonNullUrlList = ArrayList<FileProperty>()
        for(n in fileList){
            val isImage = n?.type != null && n.type.startsWith("image")
            if(isImage && n?.url != null){
                nonNullUrlList.add(n)
            }
        }
        return nonNullUrlList
    }

    private fun picasso(imageUrl: String, imageView: ImageView, isSensitive: Boolean?){
        imageView.visibility = View.VISIBLE

        if(isSensitive != null && isSensitive){
            imageView.setImageResource(R.drawable.sensitive_image)
        }else{
            Picasso
                .get()
                .load(imageUrl)
                .into(imageView)
        }

    }


}