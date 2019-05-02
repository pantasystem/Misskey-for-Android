package org.panta.misskey_for_android_v2.view_presenter.notification

import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.repository.NotificationRepository
import org.panta.misskey_for_android_v2.usecase.PagingController

class NotificationPresenter(private val mView: NotificationContract.View, info: ConnectionProperty) :
    NotificationContract.Presenter, ErrorCallBackListener{

    private val mNotification = NotificationRepository(domain = info.domain, authKey = info.i)

    private val pagingController = PagingController(mNotification, this)

    override fun start() {

    }

    override fun getNewNotification() {
        pagingController.getNewItems {
            mView.showNewNotification(it)
            mView.stopRefreshing()
        }
    }

    override fun getOldNotification() {
        pagingController.getOldItems {
            mView.showOldNotification(it)
        }
    }

    override fun initNotification() {
        pagingController.getInit {
            mView.showInitNotification(it)
        }
    }

    override fun callBack(e: Exception) {
        mView.stopRefreshing()
    }

    override fun markAllAsRead() {
        mNotification.markAllAsRead()
    }
}