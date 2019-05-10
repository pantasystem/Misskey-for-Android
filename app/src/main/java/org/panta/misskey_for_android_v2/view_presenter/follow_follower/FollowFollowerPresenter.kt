package org.panta.misskey_for_android_v2.view_presenter.follow_follower

import android.util.Log
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.interfaces.FollowFollowerContract
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.repository.UserRepository
import org.panta.misskey_for_android_v2.view_data.FollowViewData
import org.panta.misskey_for_android_v2.usecase.PagingController

class FollowFollowerPresenter(private val mView: FollowFollowerContract.View, mTimeline: IItemRepository<FollowViewData>, private val info: ConnectionProperty)
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

    override fun onFollowUnFollowButtonClicked(item: FollowViewData){
        if(item.following?.id == null){
            UserRepository(domain = info.domain, authKey = info.i).followUser(item.follower?.id!!){
                if(it){
                    mView.updateItem(item.copy(following = item.follower))
                }
            }
        }else{
            UserRepository(domain = info.domain, authKey = info.i).unFollowUser(item.following.id){
                if(it){
                    mView.removeItem(item)
                }
            }
        }
    }

    override fun callBack(e: Exception) {
        mView.showError(e)
        mView.stopRefreshing()
    }
}