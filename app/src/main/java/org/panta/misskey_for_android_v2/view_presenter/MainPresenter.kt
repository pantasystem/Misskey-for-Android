package org.panta.misskey_for_android_v2.view_presenter

import org.panta.misskey_for_android_v2.constant.ApplicationConstant
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.constant.getAppSecretKey
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.User
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator
import org.panta.misskey_for_android_v2.interfaces.MainContract
import org.panta.misskey_for_android_v2.repository.MyInfo
import org.panta.misskey_for_android_v2.util.sha256

class MainPresenter(private val mView: MainContract.View, private val sharedOperator: ISharedPreferenceOperator) : MainContract.Presenter{

    private lateinit var mUser: User

    override fun getPersonalMiniProfile() {
        val info = loadConnectInfo()
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
        val info = loadConnectInfo()
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
        val info = loadConnectInfo()
        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showEditNote(info)
        }
    }

    override fun getPersonalProfilePage() {
        val info = loadConnectInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            mView.showPersonalProfilePage(it, info)
        }
    }

    override fun getFollowFollower(type: FollowFollowerType) {
        val info = loadConnectInfo()
        if(info == null){
            mView.showAuthActivity()
        }else{
            mView.showFollowFollower(info, mUser, type)
        }
    }

    private fun loadConnectInfo(): DomainAuthKeyPair?{
        val domain = sharedOperator.get(ApplicationConstant.APP_DOMAIN_KEY, null)
        val userToken = sharedOperator.get(ApplicationConstant.APP_USER_TOKEN_KEY, null)
        return if( domain == null || userToken == null ){
            null
        }else{
            DomainAuthKeyPair(domain, sha256("$userToken${getAppSecretKey()}"))
        }
    }
}