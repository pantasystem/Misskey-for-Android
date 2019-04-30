package org.panta.misskey_for_android_v2.view_presenter.notification

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.repository.Notification
import org.panta.misskey_for_android_v2.usecase.PagingController

class NotificationPresenter(private val mView: NotificationContract.View, info: DomainAuthKeyPair) :
    NotificationContract.Presenter, ErrorCallBackListener{

    private val mNotification = Notification(domain = info.domain, authKey = info.i)

    private val pagingController = PagingController(mNotification, this)

    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNewNotification() {
        pagingController.getNewItems {
            mView.showNewNotification(it)
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

    }
}