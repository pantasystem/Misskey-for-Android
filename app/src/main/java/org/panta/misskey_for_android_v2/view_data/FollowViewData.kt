package org.panta.misskey_for_android_v2.view_data

import org.panta.misskey_for_android_v2.entity.FollowProperty
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.ID

data class FollowViewData(override val id: String, override val isIgnore: Boolean, val following: User?,val follower: User?):ID