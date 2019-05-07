package org.panta.misskey_for_android_v2.usecase

import android.annotation.SuppressLint
import android.util.Log
import android.util.SparseArray
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.interfaces.IBindScrollPosition
import org.panta.misskey_for_android_v2.interfaces.IBindStreamingAPI
import org.panta.misskey_for_android_v2.view_data.NoteViewData

class ObservationStreaming(private val bindStreamingAPI: IBindStreamingAPI, private val bindScrollPosition: IBindScrollPosition) {


    var isObserve: Boolean = true
    //このスピードでノートのキャプチャを登録するかを判定する
    private var scrollSpeed: Double = 0.0
    init{
        GlobalScope.launch{
            var beforePosition = bindScrollPosition.bindFirstVisibleItemPosition()?:0
            while(isObserve){
                delay(500)
                val nowPosition = bindScrollPosition.bindFirstVisibleItemPosition()?:0
                scrollSpeed = Math.abs(nowPosition - beforePosition) / 0.5
                beforePosition = nowPosition

                if(scrollSpeed < 4.0){
                    try{
                        captureNote()
                    }catch(e: Exception){
                        Log.w("Observation", "error", e)
                    }
                }
                //Log.d("Observation", "speed $scrollSpeed")

            }



        }
    }

    private var beforeFirst = 0
    private var beforeEnd = 0
    @SuppressLint("UseSparseArrays")
    private val sparseArray = HashMap<Int, NoteViewData>()

    private fun captureNote(){
        val firstVisiblePosition = bindScrollPosition.bindFirstVisibleItemPosition()?: 0
        val visibleTotal = bindScrollPosition.bindFindItemCount()?: 0

        //0番目と 2番目が表示されていれば firstVisiblePositionが0番目 endVisiblePositionが2番目になる

        val end = firstVisiblePosition + visibleTotal


        if(beforeFirst == firstVisiblePosition && beforeEnd == end){
            return
        }

        //登録
        for(n in firstVisiblePosition.until(end)){
            val note = bindScrollPosition.pickViewData(n)
            //Log.d("Observation", "pickしたノート $note")

            if(note != null){
                registerNote(n,note)
            }
        }

        val isUp = beforeFirst - firstVisiblePosition > 0
        if(isUp){
            //endを消す
            for(n in (end + 1)..beforeEnd){
                cancelCapture(n)
            }
        }else{
            //firstを消す
            for(n in beforeFirst.until(firstVisiblePosition)){
                cancelCapture(n)
            }
        }


        beforeFirst = firstVisiblePosition
        beforeEnd = end


    }

    private fun registerNote(index: Int,viewData: NoteViewData){
        synchronized(sparseArray){

            val hasNote = sparseArray.containsValue(viewData)
            if(hasNote){
                return
            }else{
                sparseArray.put(index, viewData)
            }
        }
    }

    private fun cancelCapture(index: Int){
        synchronized(sparseArray){
            sparseArray.remove(index)
        }
    }

}