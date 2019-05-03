package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.ReactionCreateXorDeleteProperty
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.StreamHttpsConnection
import java.net.URL

class Reaction(private val domain: String, private val authKey: String){

    private val connection =  StreamHttpsConnection()
    fun sendReaction(targetNoteId: String, type: String, callBack:(Boolean)->Unit){
        GlobalScope.launch{
            try{
                val data = ReactionCreateXorDeleteProperty( i = authKey, noteId = targetNoteId, reaction = type)
                val json = jacksonObjectMapper().writeValueAsString(data)
                val stream = connection.post(URL("$domain/api/notes/reactions/create"), json)
                if(stream == null){
                    callBack(false)
                }else{
                    callBack(true)
                }
            }catch(e: Exception){
                Log.w("Reaction", "リアクション送信中にエラーが発生しました", e)
                callBack(false)
            }

        }

    }
}