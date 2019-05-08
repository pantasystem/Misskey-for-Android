package org.panta.misskey_for_android_v2.interfaces

import android.os.Handler
import org.panta.misskey_for_android_v2.view_data.NoteViewData

interface IOperationAdapter<E> {
    fun addAllFirst(list: List<E>)

    fun addAllLast(list: List<E>)

    fun getItem(index: Int): E
    fun getItem(item: E): E
    fun updateItem(item: E)

    fun removeItem(item: E)
}