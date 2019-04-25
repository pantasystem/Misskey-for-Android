package org.panta.misskey_for_android_v2.view_presenter.timeline

import kotlinx.coroutines.Job
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.authKey
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.domain
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.interfaces.ITimeline
import org.panta.misskey_for_android_v2.repository.*
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View, private val mTimeline: ITimeline) : TimelineContract.Presenter{

    private var latestNoteData: NoteViewData? = null
    private var oldestNoteData: NoteViewData?=null


    private val mReaction = Reaction(domain = domain, authKey = authKey)


    override fun getNewTimeline() {
        if(latestNoteData == null){
            initTimeline()
            return
        }
        val job = mTimeline.getNotesUseSinceId(latestNoteData!!){
            if(it == null){
                mView.stopRefreshing()
                return@getNotesUseSinceId
            }
            mView.showNewTimeline(it)
            val latest = searchLatestNoteId(it)
            if(latest != null){
                //latestNoteId = searchLatestNoteId(it)   //latestローカル変数を渡してはいけないのか？・・
                latestNoteData = latest
            }

        }
    }

    override fun getOldTimeline() {
        if(oldestNoteData == null){
            initTimeline()
            return
        }
        val job = mTimeline.getNotesUseUntilId(oldestNoteData!!){
            if(it == null){
                mView.stopRefreshing()
                return@getNotesUseUntilId
            }
            mView.showOldTimeline(it)

            val oldest = searchOldestNoteId(it)
            if(oldest != null){
                //oldestNoteId = searchOldestNoteId(it)
                oldestNoteData = oldest
            }
        }
    }

    override fun initTimeline() {
        val job = mTimeline.getTimeline{
            if(it == null){
                mView.stopRefreshing()
                return@getTimeline
            }

            mView.showInitTimeline(it)
            val latest = searchLatestNoteId(it)
            val oldest = searchOldestNoteId(it)
            if(latest != null){
                latestNoteData = latest
            }
            if(oldest != null){
                oldestNoteData = oldest
            }

        }
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

    private fun searchLatestNoteId(list: List<NoteViewData>): NoteViewData?{
        for(n in list){
            /*if(! n.isOriginReply){
                return n
            }*/
            if(n.type != NoteAdjustment.NoteType.REPLY_TO){
                return n
            }
        }
        return null
    }

    private fun searchOldestNoteId(list: List<NoteViewData>): NoteViewData?{
        for(n in (list.size - 1).downTo(0)){
            val noteViewData = list[n]
            /*if(!noteViewData.isOriginReply){
                return noteViewData
            }*/
            if(noteViewData.type != NoteAdjustment.NoteType.REPLY_TO){
                return noteViewData
            }
        }
        return null
    }

}