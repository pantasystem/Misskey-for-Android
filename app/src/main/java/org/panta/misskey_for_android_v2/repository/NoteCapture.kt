package org.panta.misskey_for_android_v2.repository

import android.net.Uri
import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import org.panta.misskey_for_android_v2.entity.BodyProperty
import org.panta.misskey_for_android_v2.entity.ConnectionProperty
import org.panta.misskey_for_android_v2.entity.StreamingProperty
import org.panta.misskey_for_android_v2.interfaces.IBindStreamingAPI
import org.panta.misskey_for_android_v2.usecase.NoteUpdater
import org.panta.misskey_for_android_v2.view_data.NoteViewData
import java.lang.Exception
import java.net.URI
import java.net.URL

class NoteCapture(private val connectionInfo: ConnectionProperty,  private val bindStreamingProperty: IBindStreamingAPI){


    private var socket = Socket()
    init{
        socket.connect()
    }
    val tag = "StreamingChannel"

    private val captureViewData = ArrayList<NoteViewData>()
    private val noteUpdater = NoteUpdater()
    
    fun captureNote(viewData: NoteViewData){
        if(socket.isClosed){
            socket = Socket()
            socket.connect()
        }
        val data = StreamingProperty(type = "subNote",
            body = BodyProperty(id = viewData.toShowNote.id)
        )
        if(socket.isOpen){
            Log.d(tag, "送信先 $\"${connectionInfo.domain.replace("https://", "wss://")}/streaming?${connectionInfo.i}\"")
            socket.send(jacksonObjectMapper().writeValueAsString(data))
            captureViewData.add(viewData)
        }
    }
    
    fun unCaptureNote(viewData: NoteViewData){
        val data = StreamingProperty(type = "unsubNote",
            body = BodyProperty(id = viewData.toShowNote.id)
        )
        if(socket.isOpen){
            socket.send(jacksonObjectMapper().writeValueAsString(data))

        }
    }

    inner class Socket : WebSocketClient(URI("${connectionInfo.domain.replace("https://", "wss://")}/streaming?${connectionInfo.i}")){
        override fun onOpen(handshakedata: ServerHandshake?) {
            Log.d(tag, "onOpen")
        }

        override fun onMessage(message: String?) {
            Log.d(tag, "onMessage $message")
            message?: return
            try{
                val obj = jacksonObjectMapper().readValue<StreamingProperty>(message)
                if(obj.type == "noteUpdated" && obj.body.type == "reacted"){

                    //TODO 自分のリアクションか判定するようにする
                    val id = obj.body.id
                    val userId = obj.body.body?.userId!!
                    //val isMyReaction = userPrimaryId == userId
                    val reaction = obj.body.body.reaction!!
                    captureViewData.filter{
                        it.toShowNote.id == id
                    }.forEach{

                        val updatedViewData = noteUpdater.addReaction(reaction, it, false)
                        bindStreamingProperty.onUpdateNote(updatedViewData)
                    }

                }
            }catch(e: Exception){

            }
        }

        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            Log.d(tag, "close code:$code")
        }

        override fun onError(ex: Exception?) {
            Log.d(tag, "error", ex)
        }
    }




}