package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.constant.DomainAndAppSecret
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.entity.Note
import org.panta.misskey_for_android_v2.network.HttpsConnection
import org.panta.misskey_for_android_v2.network.OkHttpConnection
import org.panta.misskey_for_android_v2.usecase.NoteAdjustment
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.net.URL

class NoteRepository(private val connectionInfo: ConnectionProperty){
    private val connection = OkHttpConnection()

    fun send(property: CreateNoteProperty){
        GlobalScope.launch {
            try{
                val json = jacksonObjectMapper().writeValueAsString(property)
                Log.d("NoteRepository" , "JSON $json")
                connection.postString(URL("${connectionInfo.domain}/api/notes/create"), json)
            }catch(e: Exception){
                Log.w("NoteRepository", "送信中に問題発生", e)
            }
        }
    }

    fun getNote(noteId: String, callBack:(NoteViewData?)->Unit){
        val map = HashMap<String, String>()
        map["i"] = connectionInfo.i
        map["noteId"] = noteId
        val json = jacksonObjectMapper().writeValueAsString(map)
        GlobalScope.launch {
            try{
                val response = connection.postString(URL("${connectionInfo.domain}/api/notes/show"), json)
                if(response == null){
                    callBack(null)
                    return@launch
                }

                val responseData: Note = jacksonObjectMapper().readValue(response)
                val viewData = NoteAdjustment().createViewData(responseData)
                callBack(viewData)

            }catch(e: Exception){
                Log.w("NoteRepository","getNote", e)
            }
        }
    }
}