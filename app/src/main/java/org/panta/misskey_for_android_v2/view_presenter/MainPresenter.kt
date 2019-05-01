package org.panta.misskey_for_android_v2.view_presenter

import android.net.Uri
import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.constant.getInstanceInfoList
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator
import org.panta.misskey_for_android_v2.interfaces.MainContract
import org.panta.misskey_for_android_v2.repository.MyInfo
import org.panta.misskey_for_android_v2.repository.SecretRepository
import org.panta.misskey_for_android_v2.util.sha256

class MainPresenter(private val mView: MainContract.View, sharedOperator: ISharedPreferenceOperator) : MainContract.Presenter{

    private lateinit var mUser: User
    private val secretRepository = SecretRepository(sharedOperator)

    override fun getPersonalMiniProfile() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            mView.showPersonalMiniProfile(it)
            mUser = it
        }
    }


    override fun initDisplay() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }else{
            mView.initDisplay(info)
        }
    }
    override fun start() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun takeEditNote() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showEditNote(info)
        }
    }

    override fun getPersonalProfilePage() {
        val info = secretRepository.getConnectionInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            mView.showPersonalProfilePage(it, info)
        }
    }

    override fun getFollowFollower(type: FollowFollowerType) {
        val info  = secretRepository.getConnectionInfo()

        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showFollowFollower(info, mUser, type)
        }
    }

    override fun openMisskeyOnBrowser() {
        mView.showMisskeyOnBrowser(Uri.parse(secretRepository.getConnectionInfo()?.domain))
    }

    override fun isEnabledNotification(enabled: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}