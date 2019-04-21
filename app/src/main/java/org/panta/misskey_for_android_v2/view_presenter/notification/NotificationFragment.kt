package org.panta.misskey_for_android_v2.view_presenter.notification

import android.support.v4.app.Fragment
import org.panta.misskey_for_android_v2.entity.NotificationProperty

class NotificationFragment : Fragment(), NotificationContract.View{

    override var mPresenter: NotificationContract.Presenter = NotificationPresenter()

    override fun stopRefreshing() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showOldNotification(list: List<NotificationProperty>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNewNotification(list: List<NotificationProperty>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showInitNotification(list: List<NotificationProperty>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}