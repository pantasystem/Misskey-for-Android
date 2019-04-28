package org.panta.misskey_for_android_v2.interfaces

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.User

interface MainContract {
    interface View : BaseView<Presenter>{
        fun showPersonalProfile(user: User)
        fun showAuthActivity()
        fun initDisplay(connectionInfo: DomainAuthKeyPair)
        fun showEditNote(connectionInfo: DomainAuthKeyPair)
    }

    interface Presenter : BasePresenter{
        fun getPersonalProfile()
        fun saveConnectInfo(info: DomainAuthKeyPair)
        fun initDisplay()
        fun takeEditNote()
    }
}