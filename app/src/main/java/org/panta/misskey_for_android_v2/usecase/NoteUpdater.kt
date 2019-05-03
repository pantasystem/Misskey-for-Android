package org.panta.misskey_for_android_v2.usecase

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


        val reactionCountPair = noteAdjustment.createReactionCountPair(updatedReactionCounts)
        return viewData.copy(note = updatedNote, reactionCountPairList = reactionCountPair)

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


}