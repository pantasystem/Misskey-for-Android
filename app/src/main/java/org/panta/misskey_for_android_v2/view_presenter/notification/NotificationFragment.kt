package org.panta.misskey_for_android_v2.view_presenter.notification

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_notification.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.adapter.NotificationAdapter
import org.panta.misskey_for_android_v2.adapter.NotificationViewHolder
import org.panta.misskey_for_android_v2.entity.NotificationProperty
import org.panta.misskey_for_android_v2.view_data.NotificationViewData

class NotificationFragment : Fragment(), NotificationContract.View{

    override var mPresenter: NotificationContract.Presenter = NotificationPresenter(this)

    private lateinit var mLayoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mLayoutManager = LinearLayoutManager(context)
        mPresenter.initNotification()

    }

    override fun stopRefreshing() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showOldNotification(list: List<NotificationViewData>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showNewNotification(list: List<NotificationViewData>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showInitNotification(list: List<NotificationViewData>) {
        activity?.runOnUiThread{

            notification_view.layoutManager = mLayoutManager
            notification_view.adapter = NotificationAdapter(list)

        }
    }

    override fun onError(errorMsg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}