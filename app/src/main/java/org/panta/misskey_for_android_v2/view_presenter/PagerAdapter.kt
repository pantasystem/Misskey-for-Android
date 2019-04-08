package org.panta.misskey_for_android_v2.view_presenter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import org.panta.misskey_for_android_v2.constant.TimelineTypeEnum
import org.panta.misskey_for_android_v2.view_presenter.timeline.TimelineFragment

class PagerAdapter(private val fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){

    private val tabTitles = arrayOf<CharSequence>("Home", "Global")
    override fun getCount(): Int {
        return tabTitles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabTitles[position]
    }

    override fun getItem(p0: Int): Fragment? {
        return when(p0){
            0 -> TimelineFragment.getInstance(TimelineTypeEnum.HOME)
            1 -> TimelineFragment.getInstance(TimelineTypeEnum.GLOBAL)
            else -> null
        }
    }

}