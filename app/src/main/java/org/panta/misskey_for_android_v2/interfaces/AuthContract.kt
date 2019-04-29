package org.panta.misskey_for_android_v2.interfaces

import android.net.Uri
import org.panta.misskey_for_android_v2.entity.SessionResponse
import java.net.URI

interface AuthContract {
    interface View : BaseView<Presenter> {
        fun showBrowser(uri: Uri)
        fun onLoadSession(session: SessionResponse)
        fun onLoadUserToken(token: String)
    }

    interface Presenter : BasePresenter {

        //FIXME startで実行すればいいことなので不要か？
        fun getSession()
        fun getUserToken()
    }
}