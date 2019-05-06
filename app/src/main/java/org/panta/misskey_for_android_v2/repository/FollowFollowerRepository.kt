package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.FollowProperty
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.OkHttpConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.usecase.FollowViewDataMaker
import org.panta.misskey_for_android_v2.view_data.FollowViewData
import java.net.URL

class FollowFollowerRepository(private val userId: String, private val type: FollowFollowerType, private val connectionInfo: ConnectionProperty) : IItemRepository<FollowViewData>{

    private val httpsConnection = OkHttpConnection()
    /*private val url =if(type == FollowFollowerType.FOLLOWING){
        URL("${connectionInfo.domain}/api/users/followers")
    }else{
        URL("${connectionInfo.domain}/api/users/following")
    }*/
    private val followingUrl = URL("${connectionInfo.domain}/api/users/following")
    private val followerUrl = URL("${connectionInfo.domain}/api/users/followers")



    override fun getItems(callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["userId"] = userId

            callBack(getViewData(map))
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["sinceId"] = sinceId
            map["userId"] = userId
            callBack(getViewData(map))
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["untilId"] = untilId
            map["userId"] = userId
            callBack(getViewData(map))
        }catch(e: Exception){
            e.printStackTrace()
        }

    }

    private suspend fun getFollowFollowerInfo(map: Map<String, String>, url: URL): List<FollowProperty>?{
        val json = jacksonObjectMapper().writeValueAsString(map)
        val result = httpsConnection.postString(url, json)

        return if(result == null){
            null
        }else{
            return  jacksonObjectMapper().readValue(result)
        }

    }

    private suspend fun getViewData(map: Map<String, String>): List<FollowViewData>?{
        val followerList = getFollowFollowerInfo(map, followerUrl)
        val followingList = getFollowFollowerInfo(map, followingUrl)
        if(followerList == null || followingList == null){
            return null
        }
        return if(type == FollowFollowerType.FOLLOWER){
            FollowViewDataMaker().createFollowerViewDataList(followingList = followingList, followerList = followerList)
        }else{
            FollowViewDataMaker().createFollowingViewDataList(followingList = followingList, followerList = followerList)
        }
    }
}