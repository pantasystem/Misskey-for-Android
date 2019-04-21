package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.User

interface UserClickListener {
    fun onClickedUser(user: User)
}