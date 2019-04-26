package org.panta.misskey_for_android_v2.adapter

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.squareup.picasso.Picasso
import org.panta.misskey_for_android_v2.R

class ReactionHolder(itemView: View, private val  alwaysColorId: Int, private val hasMyReactionColorId: Int) : RecyclerView.ViewHolder(itemView){

    private val reactionIcon = itemView.findViewById<ImageButton>(R.id.reaction_image_button)
    private val reactionCount = itemView.findViewById<TextView>(R.id.reaction_count)
    private val reactionCountItem = itemView.findViewById<LinearLayout>(R.id.reaction_counter_view)
    fun showReaction(count: String, resourceId: Int, isHasMyReaction: Boolean = false){
        //reactionIcon.setImageResource(resourceId)
        Picasso
            .get()
            .load(resourceId)
            .into(reactionIcon)
        reactionCount.text = count
        if(isHasMyReaction){
            reactionCountItem.setBackgroundColor(hasMyReactionColorId)
        }else{
            reactionCountItem.setBackgroundColor(alwaysColorId)
        }
    }

}