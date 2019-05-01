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
import kotlinx.android.synthetic.main.item_reaction_counter.view.*
import org.panta.misskey_for_android_v2.R

class ReactionHolder(itemView: View, private val  alwaysColorId: Int, private val hasMyReactionColorId: Int) : RecyclerView.ViewHolder(itemView){

    private val reactionIcon = itemView.findViewById<ImageButton>(R.id.reaction_image_button)
    private val reactionStringIcon = itemView.findViewById<TextView>(R.id.reaction_type_string_view)
    private val reactionCount = itemView.findViewById<TextView>(R.id.reaction_count)
    private val reactionCountItem = itemView.findViewById<LinearLayout>(R.id.reaction_counter_view)
    fun showReaction(count: String, resourceId: Int, isHasMyReaction: Boolean = false){
        //reactionIcon.setImageResource(resourceId)
        Picasso
            .get()
            .load(resourceId)
            .into(reactionIcon)
        reactionStringIcon.visibility = View.GONE
        reactionIcon.visibility = View.VISIBLE
        reactionCount.text = count
        if(isHasMyReaction){
            reactionCountItem.setBackgroundResource(R.drawable.shape_selected_reaction_background)
            //reactionCountItem.background = itemView.resources.getDimension()
        }else{
            //reactionCountItem.setBackgroundColor(alwaysColorId)
            reactionCountItem.setBackgroundResource(R.drawable.shape_normal_reaction_background)

        }
    }

    fun showReaction(count: String, emoji: String, isHasMyReaction: Boolean = false){
        reactionIcon.visibility = View.GONE
        reactionStringIcon.visibility = View.VISIBLE
        reactionStringIcon.text = emoji
        reactionCount.text = count
        if(isHasMyReaction){
            reactionCountItem.setBackgroundColor(hasMyReactionColorId)
        }else{
            reactionCountItem.setBackgroundColor(alwaysColorId)
        }
    }

}