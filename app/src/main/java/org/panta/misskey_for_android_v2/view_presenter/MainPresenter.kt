package org.panta.misskey_for_android_v2.view_presenter

import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.interfaces.ISharedPreferenceOperator
import org.panta.misskey_for_android_v2.interfaces.MainContract
import org.panta.misskey_for_android_v2.repository.MyInfo

class MainPresenter(private val mView: MainContract.View, private val sharedOperator: ISharedPreferenceOperator) : MainContract.Presenter{
    override fun getPersonalProfile() {
        val info = loadConnectInfo()
        if(info == null){
            mView.showAuthActivity()
            return
        }
        MyInfo(domain = info.domain, authKey = info.i).getMyInfo {
            mView.showPersonalProfile(it)
        }
    }

    override fun saveConnectInfo(info: DomainAuthKeyPair) {
        sharedOperator.put("domain", info.domain)
        sharedOperator.put("i", info.i)
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

    private fun loadConnectInfo(): DomainAuthKeyPair?{
        val domain = sharedOperator.get("domain", null)
        val i = sharedOperator.get("i", null)
        return if( domain == null || i == null ){
            null
        }else{
            DomainAuthKeyPair(domain, i)
        }
    }
}