package org.panta.misskey_for_android_v2.interfaces

import kotlinx.coroutines.Job
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface ITimeline {
    fun getNotesUseSinceId(noteId: String, callBack: (timeline: List<NoteViewData>)->Unit):Job

    fun getNotesUseUntilId(noteId: String, callBack: (timeline: List<NoteViewData>)->Unit):Job

    fun getTimeline(callBack: (timeline: List<NoteViewData>) -> Unit):Job

    //新しいノートを発見すると呼び出されます
    fun listenTimelineUpdate(callBack: CallBackListener<Note>):Job
    fun removeListenTimelineUpdate()

    //投稿のキャプチャ（投票、リアクションの監視）をします
    fun addListenNoteUpdate(noteId: String, callBack: CallBackListener<String>)
    fun removeListenNoteUpdate(noteId: String, callBack: CallBackListener<String>)


}