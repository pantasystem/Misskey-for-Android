package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.User

interface MainContract {
    interface View : BaseView<Presenter>{
        fun showPersonalMiniProfile(user: User)
        fun showPersonalProfilePage(user: User, connectionInfo: DomainAuthKeyPair)
        fun showAuthActivity()
        fun initDisplay(connectionInfo: DomainAuthKeyPair)
        fun showEditNote(connectionInfo: DomainAuthKeyPair)
    }

    interface Presenter : BasePresenter{
        fun getPersonalMiniProfile()
        fun getPersonalProfilePage()
        fun saveConnectInfo(info: DomainAuthKeyPair)
        fun initDisplay()
        fun takeEditNote()
    }
}