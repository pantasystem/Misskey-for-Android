package org.panta.misskey_for_android_v2.view_presenter.user

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelineFragment

class UserPagerAdapter(fragmentManager: FragmentManager, private val userId: String, private val connectionInfo: DomainAuthKeyPair) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("概要", "タイムライン","メディア")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        Log.d("PagerAdapter", "getPageTitle index:$position")
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {
        val tmp = p0 % 3
        return when(tmp){
            0 ->{
                Log.d("PagerAdapter", "Localを表示中")
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL)
            }
            1 ->{
                Log.d("PagerAdapter", "SOCIALを表示中")
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.USER, userId)
            }
            2 ->{
                TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.USER, userId, true)
            }

            else -> TimelineFragment.getInstance(connectionInfo, TimelineTypeEnum.LOCAL)


        }
    }

}