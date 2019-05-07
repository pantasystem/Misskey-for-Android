package org.panta.misskey_for_android_v2.view_presenter.timeline

import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.interfaces.IBindScrollPosition
import org.panta.misskey_for_android_v2.interfaces.IBindStreamingAPI
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.repository.*
import org.panta.misskey_for_android_v2.usecase.NoteAdjustment
import org.panta.misskey_for_android_v2.usecase.NoteUpdater
import org.panta.misskey_for_android_v2.usecase.PagingController
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View,
                        override var bindScrollPosition: IBindScrollPosition, private val mTimeline: IItemRepository<NoteViewData>, info: ConnectionProperty)
    : TimelineContract.Presenter, ErrorCallBackListener, IBindStreamingAPI{


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

    override fun sendReaction(noteId: String, viewData: NoteViewData, reactionType: String) {
        mReaction.sendReaction(noteId, reactionType){
            if(it){
                Log.d("TimelinePresenter", "sendReaction成功したようだ")
                val updatedNote = NoteUpdater().addReaction(reactionType, viewData, hasMyReaction = true)
                mView.showUpdatedNote(updatedNote)
            }else{
                mView.onError("リアクションの送信に失敗した")
                Log.d("TimelinePresenter", "sendReaction失敗しちゃった・・")
            }
        }
        //mView.showUpdatedNote(viewData)


    }
    override fun captureNote(noteId: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun addFirstNote(data: NoteViewData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRemoveNote(data: NoteViewData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onUpdateNote(data: NoteViewData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}