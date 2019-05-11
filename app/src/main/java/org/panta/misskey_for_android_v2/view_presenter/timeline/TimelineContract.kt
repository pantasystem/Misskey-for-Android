package org.panta.misskey_for_android_v2.view_presenter.timeline

import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.interfaces.BasePresenter
import org.panta.misskey_for_android_v2.interfaces.BaseView
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface TimelineContract{

    interface View : BaseView<Presenter>{
        fun showNewTimeline(list: List<NoteViewData>)
        fun showOldTimeline(list: List<NoteViewData>)
        fun showInitTimeline(list: List<NoteViewData>)
        fun stopRefreshing()
        fun onError(errorMsg: String)
        fun showUpdatedNote(noteViewData: NoteViewData)
        fun showReactionSelectorView(targetId: String, viewData: NoteViewData)
    }

    interface Presenter : BasePresenter {
        fun getNewTimeline()
        fun getOldTimeline()
        fun initTimeline()
        fun captureNote(noteId: String)
        fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String)
        fun setReactionSelectedState(targetId: String?, note: Note?, viewData: NoteViewData, reactionType: String?)
        fun onRefresh()
    }
}