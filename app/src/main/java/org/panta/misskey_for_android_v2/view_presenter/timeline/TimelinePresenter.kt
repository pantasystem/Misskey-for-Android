package org.panta.misskey_for_android_v2.view_presenter.timeline

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.repository.*
import org.panta.misskey_for_android_v2.usecase.PagingController
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View, private val mTimeline: IItemRepository<NoteViewData>, info: DomainAuthKeyPair)
    : TimelineContract.Presenter, ErrorCallBackListener{

    private val pagingController =
        PagingController<NoteViewData>(mTimeline, this)


    private val mReaction = Reaction(domain = info.domain, authKey = info.i)


    override fun getNewTimeline() {
        pagingController.getNewItems {
            mView.showNewTimeline(it)
        }
    }

    override fun getOldTimeline() {
        pagingController.getOldItems {
            mView.showOldTimeline(it)
        }
    }

    override fun initTimeline() {
        pagingController.getInit {
            mView.showInitTimeline(it)
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }

    override fun sendReaction(noteId: String, reactionType: String) {
        mReaction.sendReaction(noteId, reactionType)
    }

    override fun captureNote(noteId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



}