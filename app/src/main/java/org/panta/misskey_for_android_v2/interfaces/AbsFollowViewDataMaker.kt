package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.FollowProperty
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.view_data.FollowViewData

abstract class AbsFollowViewDataMaker{

    fun createFollowingViewDataList(followingList: List<FollowProperty>, followerList: List<FollowProperty>): List<FollowViewData>{
        val followerUsers = followerList.map{
            it.follower
        }
        return createList(followingList, followerUsers, FollowFollowerType.FOLLOWING)
    }

    fun createFollowerViewDataList(followingList: List<FollowProperty>, followerList: List<FollowProperty>): List<FollowViewData>{
        val followingUsers = followingList.map{
            it.followee
        }
        return createList(followerList, followingUsers, FollowFollowerType.FOLLOWER)
    }

    abstract fun createList(baseList: List<FollowProperty>, addList: List<User?>, type: FollowFollowerType): List<FollowViewData>
}