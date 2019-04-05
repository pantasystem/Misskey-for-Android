package org.panta.misskey_for_android_v2.repository

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.entity.CreateNoteProperty
import org.panta.misskey_for_android_v2.network.HttpsConnection
import java.net.URL

class NoteRepository(private val domain: String){
    private val connection = HttpsConnection()

    fun send(property: CreateNoteProperty){
        GlobalScope.launch {
            try{
                val json = jacksonObjectMapper().writeValueAsString(property)
                Log.d("NoteRepository" , "JSON $json")
                connection.post(URL("$domain/api/notes/create"), json)
            }catch(e: Exception){
                Log.w("NoteRepository", "送信中に問題発生", e)
            }
        }
    }
}