package org.panta.misskey_for_android_v2.view_presenter.follow_follower

import org.panta.misskey_for_android_v2.interfaces.BasePresenter
import org.panta.misskey_for_android_v2.interfaces.BaseView
import org.panta.misskey_for_android_v2.view_data.FollowViewData

interface FollowFollowerContract{
    interface Presenter : BasePresenter{
        fun getOldItems()
        fun getNewItems()
        fun getItems()
    }

    interface View : BaseView<Presenter>{
        fun showOldItems(list: List<FollowViewData>)
        fun showNewItems(list: List<FollowViewData>)
        fun showItems(list: List<FollowViewData>)
        fun showError(e: Exception)
        fun stopRefreshing()

    }
}