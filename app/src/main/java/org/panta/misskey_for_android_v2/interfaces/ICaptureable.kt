package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface ICapture{
    fun captureNote(noteViewData: NoteViewData)
    fun cancelCaptureNote(noteViewData: NoteViewData): Boolean
}