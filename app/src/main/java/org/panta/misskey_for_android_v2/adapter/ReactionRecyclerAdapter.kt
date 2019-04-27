package org.panta.misskey_for_android_v2.adapter

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.entity.ReactionCountPair

class ReactionRecyclerAdapter(private val reactionList: List<ReactionCountPair>, private val myReactionType: String?) : RecyclerView.Adapter<ReactionHolder>(){

    /*private val reactionList = reactionCountMap.map{
        it.key to it.value
    }*/
    private val reactionImageMapping = hashMapOf("like" to R.drawable.reaction_icon_like ,
        "love" to R.drawable.reaction_icon_love ,
        "laugh" to R.drawable.reaction_icon_laugh,
        "hmm" to R.drawable.reaction_icon_hmm,
        "surprise" to R.drawable.reaction_icon_surprise ,
        "congrats" to R.drawable.reaction_icon_congrats,
        "angry" to R.drawable.reaction_icon_angry,
        "confused" to R.drawable.reaction_icon_confused,
        "rip" to R.drawable.reaction_icon_rip,
        "pudding" to R.drawable.reaction_icon_pudding)

    override fun getItemCount(): Int {
        return reactionList.size
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReactionHolder {
        val inflater = LayoutInflater.from(p0.context).inflate(R.layout.item_reaction_counter, p0, false)

        return ReactionHolder(inflater, alwaysColorId = Color.parseColor("#00FFFFFF"), hasMyReactionColorId = Color.parseColor("#ff6100"))
    }

    override fun onBindViewHolder(p0: ReactionHolder, position: Int) {
        val reaction = reactionList[position]
        val isMyReaction = reaction.reactionType == myReactionType
        //val drawable = Drawable.createFromPath("reaction_icon_${reaction.reactionType}.png")
        val icon = reactionImageMapping[reaction.reactionType]
        if(icon != null){
            p0.showReaction(reaction.reactionCount, icon, isMyReaction)
        }else{
            p0.showReaction(reaction.reactionCount, R.drawable.human_icon, isMyReaction)
        }

    }
}