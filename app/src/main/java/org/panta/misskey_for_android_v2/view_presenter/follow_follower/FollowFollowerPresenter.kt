package org.panta.misskey_for_android_v2.view_presenter.follow_follower

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.interfaces.FollowFollowerContract
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.view_data.FollowViewData
import org.panta.misskey_for_android_v2.view_presenter.timeline.PagingController

class FollowFollowerPresenter(private val mView: FollowFollowerContract.View, mTimeline: IItemRepository<FollowViewData>, info: DomainAuthKeyPair)
    : FollowFollowerContract.Presenter, ErrorCallBackListener{

    private val pagingController = PagingController(mTimeline, this)
    override fun getItems() {
        pagingController.getInit {
            mView.showItems(it)
            mView.stopRefreshing()
        }
    }

    override fun getNewItems() {
        pagingController.getNewItems {
            mView.showNewItems(it)
            mView.stopRefreshing()
        }
    }

    override fun getOldItems() {
        pagingController.getOldItems {
            mView.showOldItems(it)
            mView.stopRefreshing()
        }
    }

    override fun start() {

    }

    override fun callBack(e: Exception) {
        mView.showError(e)
        mView.stopRefreshing()
    }
}