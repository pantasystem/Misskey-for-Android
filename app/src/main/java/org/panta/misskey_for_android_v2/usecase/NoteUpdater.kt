package org.panta.misskey_for_android_v2.usecase

import org.panta.misskey_for_android_v2.entity.ReactionCountPair
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class NoteUpdater{

    private val noteAdjustment = NoteAdjustment(false)

    fun addReaction(reaction: String, viewData: NoteViewData, hasMyReaction: Boolean): NoteViewData{
        val note = viewData.toShowNote
        val updatedReactionCounts = updateUpdateReactionCounts(reaction, note.reactionCounts)

        val updatedNote = if(hasMyReaction){
            note.copy(myReaction = reaction, reactionCounts = updatedReactionCounts)
        }else{
            note.copy(reactionCounts = updatedReactionCounts)
        }


        val reactionCountPair = updateUpdateReactionCountPair(reaction,viewData.reactionCountPairList)
        return viewData.copy(toShowNote = updatedNote, reactionCountPairList = reactionCountPair)

    }

    private fun updateUpdateReactionCounts(reaction: String, map: Map<String, Int>?): Map<String, Int>{
        return if(map != null && map.isNotEmpty()){
            val hashMap = HashMap<String, Int>(map)
            val hasReaction = hashMap[reaction]
            if(hasReaction != null){
                hashMap[reaction] = hasReaction + 1
            }else{
                hashMap[reaction] = 1
            }
            hashMap
        }else{
            hashMapOf(reaction to 1)
        }
    }

    private fun updateUpdateReactionCountPair(reaction: String, pairList: List<ReactionCountPair>): List<ReactionCountPair>{
        val arrayList = ArrayList<ReactionCountPair>(pairList)
        val count = pairList.filter{
            it.reactionType == reaction
        }.count()
        if(count > 0){
            for(n in 0 until arrayList.size){
                if(arrayList[n].reactionType == reaction){
                    val reactionCount = Integer.parseInt(arrayList[n].reactionCount) + 1
                    arrayList[n] = arrayList[n].copy(reactionCount = reactionCount.toString())
                }
            }
        }else{
            arrayList.add(ReactionCountPair(reaction, 1.toString()))
        }
        return arrayList

    }



}