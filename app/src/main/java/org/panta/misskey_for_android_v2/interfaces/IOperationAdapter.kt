package org.panta.misskey_for_android_v2.interfaces

import android.os.Handler
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface IOperationAdapter<E> {
    fun addAllFirst(list: List<E>)

    fun addAllLast(list: List<E>)

    fun getNote(index: Int): E
    fun updateNote(item: E)

    fun removeNote(item: E)
}