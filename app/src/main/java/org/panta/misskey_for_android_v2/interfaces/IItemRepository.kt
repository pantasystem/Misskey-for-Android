package org.panta.misskey_for_android_v2.interfaces

import kotlinx.coroutines.Job
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface IItemRepository<E: ID> {
    fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<E>?)->Unit):Job

    fun getItems(callBack: (timeline: List<E>?) -> Unit):Job


}