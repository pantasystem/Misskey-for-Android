package org.panta.misskey_for_android_v2.constant

enum class FollowFollowerType {
    FOLLOWING, FOLLOWER;
    companion object{
        fun getTypeFromOrdinal(ordinal: Int): FollowFollowerType{
            return when(ordinal){
                FOLLOWER.ordinal -> FOLLOWER
                FOLLOWING.ordinal -> FOLLOWING
                else -> FOLLOWING
            }
        }
    }
}