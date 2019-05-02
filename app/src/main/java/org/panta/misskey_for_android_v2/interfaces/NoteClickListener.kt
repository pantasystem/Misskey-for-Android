package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.Note
import java.util.*

interface NoteClickListener {
    fun onNoteClicked(targetId: String?, note: Note?)
    fun onReplyButtonClicked(targetId: String?, note: Note?)
    fun onReNoteButtonClicked(targetId: String?, note: Note?)
    fun onReactionClicked(targetId: String?, note: Note?, reactionType: String?)
    fun onDescriptionButtonClicked(targetId: String?, note: Note?)
    fun onImageClicked(clickedIndex: Int, clickedImageUrlCollection: Array<String>)

}