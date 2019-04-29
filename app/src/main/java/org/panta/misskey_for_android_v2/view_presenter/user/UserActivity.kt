package org.panta.misskey_for_android_v2.view_presenter.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_user.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.repository.UserRepository
import org.panta.misskey_for_android_v2.view_presenter.follow_follower.FollowFollowerActivity
import java.lang.IllegalArgumentException

class UserActivity : AppCompatActivity() {

    companion object{
        private const val USER_PROPERTY_TAG = "UserActivityUserPropertyTag"
        private const val CONNECTION_INFO = "UserActivityConnectionInfo"

        fun startActivity(context: Context?, user: User, info: DomainAuthKeyPair){
            val intent = Intent(context, UserActivity::class.java)
            intent.putExtra(USER_PROPERTY_TAG, user)
            intent.putExtra(CONNECTION_INFO, info)
            context?.startActivity(intent)
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(toolbar)

        val intent = intent
        val tmpUser: User? = intent.getSerializableExtra(USER_PROPERTY_TAG) as User
        val info = intent.getSerializableExtra(CONNECTION_INFO) as DomainAuthKeyPair
        if(tmpUser == null){
            finish()
            throw IllegalArgumentException("user propertyがNULL")
        }

        UserRepository(info.domain, info.i).getUserInfo(tmpUser.id){
            runOnUiThread{
                Picasso.get().load(it.avatarUrl).into(profile_icon)

                profile_user_id.text = if(it.host == null){
                    "@${it.userName}"
                }else{
                    "@${it.userName}@${it.host}"
                }
                profile_user_name.text = it.name ?: it.userName
                profile_description.text = it.description
                profile_follow_count.text = "${it.followingCount} フォロー"
                profile_follower_count.text = "${it.followersCount} フォロワー"
                posts_count.text = "${it.notesCount} 投稿"
                profile_age.visibility = View.GONE

                Picasso.get().load(it.bannerUrl).into(header_image)
            }

        }

        val ad = profile_view_pager.adapter
        val adapter = if(ad == null){
            UserPagerAdapter(supportFragmentManager, tmpUser.id, info)
        }else{
            ad.notifyDataSetChanged()
            ad
        }
        profile_view_pager.offscreenPageLimit = 3
        profile_view_pager.adapter = adapter

        profile_tab.setupWithViewPager(profile_view_pager)



    }

    private fun showFollowList(info: DomainAuthKeyPair, user: User, type: FollowFollowerType){
        val intent = Intent(applicationContext, FollowFollowerActivity::class.java)
        intent.putExtra(FollowFollowerActivity.CONNECTION_INFO, info)
        intent.putExtra(FollowFollowerActivity.FOLLOW_FOLLOWER_TYPE, type)
        intent.putExtra(FollowFollowerActivity.USER_ID_TAG, user.id)
    }

}
