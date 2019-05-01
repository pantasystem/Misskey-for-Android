package org.panta.misskey_for_android_v2.view_presenter.follow_follower

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_follow_follower.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.ConnectionProperty

class FollowFollowerActivity : AppCompatActivity() {

    companion object{
        const val USER_ID_TAG = "FollowFollowerActivityUserIdTag"
        const val FOLLOW_FOLLOWER_TYPE = "FollowFollowerActivityFollowingFollowerType"
        const val CONNECTION_INFO = "FollowFollowerActivityConnectionInfo"

        fun startActivity(context: Context, info: ConnectionProperty, type: FollowFollowerType, userId: String){
            val intent = Intent(context, FollowFollowerActivity::class.java)
            intent.putExtra(FollowFollowerActivity.CONNECTION_INFO, info)
            intent.putExtra(FollowFollowerActivity.FOLLOW_FOLLOWER_TYPE, type.ordinal)
            intent.putExtra(FollowFollowerActivity.USER_ID_TAG, userId)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_follower)

        val userId = intent.getStringExtra(USER_ID_TAG)!!
        val tmpType = intent.getIntExtra(FOLLOW_FOLLOWER_TYPE, FollowFollowerType.FOLLOWING.ordinal)
        val connectionInfo = intent.getSerializableExtra(CONNECTION_INFO) as ConnectionProperty
        val type = FollowFollowerType.getTypeFromOrdinal(tmpType)

        val adapter = FollowPagerAdapter(supportFragmentManager, connectionInfo, userId)
        follow_follower_pageer.offscreenPageLimit = 2
        follow_follower_pageer.adapter = adapter
        follow_follower_tab.setupWithViewPager(follow_follower_pageer)
        follow_follower_pageer.currentItem = if(type == FollowFollowerType.FOLLOWER) 1 else 0

    }
}
