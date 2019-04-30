package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.User

//別の独自のインターフェースと統合予定
interface UserClickListener {
    fun onClickedUser(user: User)
}