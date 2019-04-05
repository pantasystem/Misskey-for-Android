package org.panta.misskey_for_android_v2.view_presenter.timeline

import org.panta.misskey_for_android_v2.repository.AbsTimeline
import org.panta.misskey_for_android_v2.repository.HomeTimeline
import org.panta.misskey_for_android_v2.repository.Reaction
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.authKey
import org.panta.misskey_for_android_v2.constant.ApplicationConstant.domain
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class TimelinePresenter(private val mView: TimelineContract.View) : TimelineContract.Presenter{

    private var latestNoteId: String? = null
    private var oldestNoteId: String?=null

    private val mTimeline: AbsTimeline = HomeTimeline(domain = ApplicationConstant.domain , authKey = ApplicationConstant.authKey)
    private val mReaction = Reaction(domain = domain, authKey = authKey)


    override fun getNewTimeline() {
        if(latestNoteId == null){
            initTimeline()
            return
        }
        mTimeline.getNotesUseSinceId(latestNoteId!!){
            mView.showNewTimeline(it)
            val latest = searchLatestNoteId(it)
            if(latest != null){
                latestNoteId = searchLatestNoteId(it)
            }
        }

    }

    override fun getOldTimeline() {
        if(oldestNoteId == null){
            initTimeline()
            return
        }
        mTimeline.getNotesUseUntilId(oldestNoteId!!){
            mView.showOldTimeline(it)

            val oldest = searchOldestNoteId(it)
            if(oldest != null){
                oldestNoteId = searchOldestNoteId(it)
            }

        }
    }

    override fun initTimeline() {
        mTimeline.getTimeline{
            mView.showInitTimeline(it)


            val latest = searchLatestNoteId(it)
            val oldest = searchOldestNoteId(it)
            if(latest != null){
                latestNoteId = latest
            }
            if(oldest != null){
                oldestNoteId = oldest
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

    private fun searchLatestNoteId(list: List<NoteViewData>): String?{
        for(n in list){
            if(! n.isOriginReply){
                return n.note.id
            }
        }
        return null
    }

    private fun searchOldestNoteId(list: List<NoteViewData>): String?{
        for(n in (list.size - 1).downTo(0)){
            val noteViewData = list[n]
            if(!noteViewData.isOriginReply){
                return noteViewData.note.id
            }
        }
        return null
    }

}