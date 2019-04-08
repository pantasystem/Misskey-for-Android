package org.panta.misskey_for_android_v2.interfaces

import kotlinx.coroutines.Job
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface ITimeline {
    fun getNotesUseSinceId(noteId: String, callBack: (timeline: List<NoteViewData>?)->Unit):Job

    fun getNotesUseUntilId(noteId: String, callBack: (timeline: List<NoteViewData>?)->Unit):Job

    fun getTimeline(callBack: (timeline: List<NoteViewData>?) -> Unit):Job


}