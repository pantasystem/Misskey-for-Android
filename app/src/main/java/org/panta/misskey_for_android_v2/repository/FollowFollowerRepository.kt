package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.constant.FollowFollowerType
import org.panta.misskey_for_android_v2.entity.DomainAuthKeyPair
import org.panta.misskey_for_android_v2.entity.FollowProperty
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamConverter
import org.panta.misskey_for_android_v2.view_data.FollowViewData
import java.net.URL

class FollowFollowerRepository(private val userId: String, private val type: FollowFollowerType, private val connectionInfo: DomainAuthKeyPair) : IItemRepository<FollowViewData>{

    private val httpsConnection = HttpsConnection()
    private val url =if(type == FollowFollowerType.FOLLOWING){
        URL("${connectionInfo.domain}/api/users/followers")
    }else{
        URL("${connectionInfo.domain}/api/users/following")
    }

    override fun getItems(callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["userId"] = userId
            callBack(getFollowFollowerInfo(map, url))
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemsUseSinceId(sinceId: String, callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["sinceId"] = sinceId
            map["userId"] = userId
            callBack(getFollowFollowerInfo(map, url))
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    override fun getItemsUseUntilId(untilId: String, callBack: (timeline: List<FollowViewData>?) -> Unit) = GlobalScope.launch{
        try{
            val map = HashMap<String, String>()
            map["untilId"] = untilId
            map["userId"] = userId
            callBack(getFollowFollowerInfo(map, url))
        }catch(e: Exception){
            e.printStackTrace()
        }

    }

    private suspend fun getFollowFollowerInfo(map: Map<String, String>, url: URL): List<FollowViewData>{
        val json = jacksonObjectMapper().writeValueAsString(map)
        val inputStream = httpsConnection.post(url, json)
        val text = StreamConverter().getString(inputStream)
        Log.d("FollowFollowerRepository", "getFollowFollowerInfo: $text")
        val propertyList: List<FollowProperty> =  jacksonObjectMapper().readValue(text)
        return propertyList.map{
            FollowViewData(it.id, false, it)
        }
    }
}