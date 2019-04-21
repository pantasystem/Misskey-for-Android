package org.panta.misskey_for_android_v2.view_presenter.notification

import org.panta.misskey_for_android_v2.entity.NotificationProperty
import org.panta.misskey_for_android_v2.interfaces.BasePresenter
import org.panta.misskey_for_android_v2.interfaces.BaseView

interface NotificationContract {
    interface View : BaseView<Presenter>{
        fun showOldNotification(list: List<NotificationProperty>)
        fun showNewNotification(list: List<NotificationProperty>)
        fun showInitNotification(list: List<NotificationProperty>)
        fun stopRefreshing()
        fun onError(errorMsg: String)
    }

    interface Presenter : BasePresenter{
        fun getOldNotification()
        fun getNewNotification()
        fun initNotification()
    }
}