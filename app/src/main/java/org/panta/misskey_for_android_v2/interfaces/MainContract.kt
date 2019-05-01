package org.panta.misskey_for_android_v2.interfaces

import android.net.Uri
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.User

interface MainContract {
    interface View : BaseView<Presenter>{
        fun showPersonalMiniProfile(user: User)
        fun showPersonalProfilePage(user: User, connectionInfo: ConnectionProperty)
        fun showAuthActivity()
        fun initDisplay(connectionInfo: ConnectionProperty)
        fun showEditNote(connectionInfo: ConnectionProperty)
        fun showFollowFollower(connectionInfo: ConnectionProperty, user: User, type: FollowFollowerType)
        fun showMisskeyOnBrowser(url: Uri)
    }

    interface Presenter : BasePresenter{
        fun getPersonalMiniProfile()
        fun getPersonalProfilePage()
        //fun saveConnectInfo(domain: String, userToken:String)
        fun initDisplay()
        fun takeEditNote()
        fun getFollowFollower(type: FollowFollowerType)
        fun openMisskeyOnBrowser()
        fun isEnabledNotification(enabled: Boolean)
    }
}