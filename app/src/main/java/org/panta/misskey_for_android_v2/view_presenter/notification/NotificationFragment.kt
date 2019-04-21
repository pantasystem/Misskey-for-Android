package org.panta.misskey_for_android_v2.view_presenter.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.panta.misskey_for_android_v2.entity.NotificationProperty

class NotificationFragment : Fragment(), NotificationContract.View{

    override var mPresenter: NotificationContract.Presenter = NotificationPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

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