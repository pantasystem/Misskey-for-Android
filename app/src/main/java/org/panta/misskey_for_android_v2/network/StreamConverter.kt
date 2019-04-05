package org.panta.misskey_for_android_v2.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class StreamConverter{

    fun getString(inputStream: InputStream): String{
        val br = BufferedReader(InputStreamReader(inputStream))
        val sb = StringBuilder()
        while(true){
            val text: String? = br.readLine()
            if(text != null){
                sb.append(text)
            }else{
                return sb.toString()
            }
        }
    }

    fun getBitmap(inputStream: InputStream): Bitmap{
        return BitmapFactory.decodeStream(inputStream)
    }
}