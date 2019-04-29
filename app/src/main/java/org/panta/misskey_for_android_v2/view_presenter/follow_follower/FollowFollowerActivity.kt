package org.panta.misskey_for_android_v2.view_presenter.follow_follower

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.FollowFollowerType

class FollowFollowerActivity : AppCompatActivity() {

    companion object{
        const val USER_ID_TAG = "FollowFollowerActivityUserIdTag"
        const val FOLLOW_FOLLOWER_TYPE = "FollowFollowerActivityFollowingFollowerType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_follower)

        val userId = intent.getStringExtra(USER_ID_TAG)!!
        val tmpType = intent.getIntExtra(FOLLOW_FOLLOWER_TYPE, FollowFollowerType.FOLLOWING.ordinal)
        val type = FollowFollowerType.getTypeFromOrdinal(tmpType)


    }
}
