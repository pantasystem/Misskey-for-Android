package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.view_data.NoteViewData

//TimelineとStreamingAPIを橋渡しするPresenter、Viewに実装する。
interface IBindStreamingAPI {
    var bindScrollPosition: IBindScrollPosition

    fun onUpdateNote(data: NoteViewData)
    fun onRemoveNote(data: NoteViewData)
    fun addFirstNote(data: NoteViewData)
}