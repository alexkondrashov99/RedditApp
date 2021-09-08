package com.example.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

object Utils {

    fun getImageNameFromURL(url:String):String{
        val sb = StringBuilder()
        for(i in (0 until url.length).reversed()){
            if(url[i] == '/')
                break;
            sb.insert(0,url[i])
        }
        return sb.toString()
    }
    fun getBitmapFromURL(src: String?): Bitmap? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            // Log exception
            null
        }
    }
    fun getByteArrayFromURL(src: String?): ByteArray? {
        return try {
            val url = URL(src)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.setDoInput(true)
            connection.connect()
            val input: InputStream = connection.inputStream
            input.readBytes()
        } catch (e: IOException) {
            // Log exception
            null
        }
    }
    fun getBitmapFromByteArray(byteArray: ByteArray): Bitmap? {
        return try {
            BitmapFactory.decodeStream(byteArray.inputStream())
        } catch (e: IOException) {
            // Log exception
            null
        }
    }

}