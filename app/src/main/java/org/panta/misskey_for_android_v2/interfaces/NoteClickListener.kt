package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.Note

interface NoteClickListener {
    fun onNoteClicked(targetId: String?, note: Note?)
    fun onReplyButtonClicked(targetId: String?, note: Note?)
    fun onReNoteButtonClicked(targetId: String?, note: Note?)
    fun onReactionButtonClicked(targetId: String?, note: Note?)
    fun onDescriptionButtonClicked(targetId: String?, note: Note?)

}