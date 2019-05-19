package org.panta.misskey_for_android_v2.repository

import kotlinx.coroutines.Job
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class MessageRepository : IItemRepository<NoteViewData>{
    fun create(){

    }

    override fun getItems(callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<NoteViewData>?) -> Unit): Job {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}