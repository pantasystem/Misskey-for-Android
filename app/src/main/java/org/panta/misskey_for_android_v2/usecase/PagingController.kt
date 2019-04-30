package org.panta.misskey_for_android_v2.usecase

import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.interfaces.ID
import org.panta.misskey_for_android_v2.interfaces.IItemRepository
import java.lang.Exception

class PagingController<E: ID>(private val mRepository: IItemRepository<E>, private val errorCallBackListener: ErrorCallBackListener){
    private var latestId: String? = null
    private var oldestId: String? = null

    fun getNewItems(callBack:(List<E>)->Unit){
        if(latestId == null){
            getInit(callBack)
            return
        }
        mRepository.getItemsUseSinceId(latestId!!){
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItemsUseSinceId
            }
            callBack(it)
            val l = searchLatestId(it)
            if(l != null){
                latestId = l
            }
        }
    }
    fun getOldItems(callBack:(List<E>)->Unit){
        if( oldestId == null ){
            getInit(callBack)
            return
        }
        mRepository.getItemsUseUntilId(oldestId!!){
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItemsUseUntilId
            }
            callBack(it)

            val o = searchOldestId(it)
            if(o != null){
                oldestId = o
            }
        }
    }
    fun getInit(callBack:(List<E>)->Unit){
        mRepository.getItems {
            if(it == null){
                errorCallBackListener.callBack(Exception("NULL返ってきちゃった・・"))
                return@getItems
            }
            callBack(it)

            val l = searchLatestId(it)
            val o = searchOldestId(it)
            if(l != null){
                latestId = l
            }
            if(o != null){
                oldestId = o
            }

        }
    }

    private fun searchLatestId(list: List<ID>): String?{
        for(n in list){

            if(! n.isIgnore ){
                return n.id
            }
        }
        return null
    }

    private fun searchOldestId(list: List<ID>): String?{
        for(n in (list.size - 1).downTo(0)){
            val data = list[n]
            /*if(!noteViewData.isOriginReply){
                return noteViewData
            }*/
            if(!data.isIgnore){
                return data.id
            }
        }
        return null
    }

}