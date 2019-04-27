package org.panta.misskey_for_android_v2.view_presenter.mixed_timeline

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_mixed_timeline.*
import org.panta.misskey_for_android_v2.R
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair

class MixedTimelineFragment: Fragment(){

    companion object{
        private const val CONNECTION_INFO = "MixedTimelineFragmentConnectionInfo"
        fun getInstance(info: DomainAuthKeyPair): MixedTimelineFragment{
            val fragment = MixedTimelineFragment()
            fragment.arguments = Bundle().apply {
                putSerializable(CONNECTION_INFO, info)
            }
            return fragment
        }
    }

    private lateinit var connectionInfo: DomainAuthKeyPair

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        connectionInfo = arguments!!.getSerializable(CONNECTION_INFO) as DomainAuthKeyPair
        return inflater.inflate(R.layout.fragment_mixed_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("MixedTimelineFragment", "onViewCreated")

        val ad = mix_timeline_view_pager.adapter
        val adapter = if(ad == null){
            PagerAdapter(childFragmentManager, connectionInfo)
        }else{
            ad.notifyDataSetChanged()
            ad
        }
        mix_timeline_view_pager.offscreenPageLimit = 3
        mix_timeline_view_pager.adapter = adapter

        mix_timeline_tab.setupWithViewPager(mix_timeline_view_pager)
    }
}