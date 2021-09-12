package com.example.redditapp.api

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.redditapp.FeedDatabaseDisplayer
import com.example.redditapp.setRedditAfterPreferences
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


const val imageDirectory = "/images"






const val json_title = "title"
const val json_author = "author"
const val json_created_utc = "created_utc"
const val json_num_comments = "num_comments"
const val json_url = "permalink"
const val json_upvotes = "ups"
const val json_subreddit = "subreddit_name_prefixed"
const val json_thumbnail = "thumbnail"
const val json_thumbnail_width = "thumbnail_width"
const val json_thumbnail_height = "thumbnail_height"

const val BASE_URL = "https://www.reddit.com"

interface FeedNetworkDisplayer {
    fun onNetworkLoadFinished(data:ArrayList<RedditData>)
    fun onNetworkError(error:String)
    fun getParentContext():Context
}
private interface RedditRequest {
    @GET
    suspend fun getRedditTop(@Url url: String?): JsonObject
}
enum class REDDIT_T(val value:String){
    HOUR("hour"),
    DAY("day"),
    WEEK("week"),
    MONTH("month"),
    YEAR("year"),
    ALL("all")
}

private fun getBitmapFromURL(src: String?): Bitmap? {
    return try {
        val url = URL(src)
        val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
        connection.setDoInput(true)
        connection.connect()
        val input: InputStream = connection.getInputStream()
        BitmapFactory.decodeStream(input)
    } catch (e: IOException) {
        // Log exception
        null
    }
}
private fun getRequestURL(limit: Int, t:REDDIT_T, count:Int = 0, before: String = "", after:String = ""):String {
    val sb = StringBuilder(BASE_URL)
    sb.append("""/top.json?""")
    sb.append("t=${t.value}")

    if(limit>0)
        sb.append("&limit=$limit")
    if(count!=0 && count > 0)
        sb.append("&count=$count")
    if(!before.isEmpty())
        sb.append("&before=$before")
    if(!after.isEmpty())
        sb.append("&after=$after")

    return sb.toString();
}
private fun getDataFromJson(jsonObject: JsonObject,activity:FeedNetworkDisplayer):ArrayList<RedditData> {
    val count = jsonObject["data"].asJsonObject["children"].asJsonArray.size();
    val dataList = ArrayList<RedditData>()
    val jsonArr = jsonObject["data"].asJsonObject["children"].asJsonArray
    for(i in 0 until count){

        val data = jsonArr[i].asJsonObject["data"].asJsonObject
        val title = data[json_title].toString().trim('"')
        val author = data[json_author].toString().trim('"')
        val createdUTC = data[json_created_utc].toString().toDouble().toLong()
        val numComments = data[json_num_comments].toString().toInt()
        val url = data[json_url].toString().trim('"')
        val upvotes = data[json_upvotes].toString().toInt()
        val subreddit = data[json_subreddit].toString().trim('"')
        val thumbnailURL = data[json_thumbnail].toString().trim('"')
        var imageBitmap:Bitmap? = null
        var imagePath:String = ""
        if(data[json_thumbnail_width].toString() != "null"){
            imageBitmap = getBitmapFromURL(thumbnailURL)
            if(imageBitmap != null){
                val imgName = getImageNameFromURL(thumbnailURL)
                val imagePathNullable = saveImage(imageBitmap,imgName,activity)
                if(imagePathNullable != null){
                    imagePath = imagePathNullable
                }
            }
        }
        val rData = RedditData(title,author,createdUTC,numComments,url,subreddit,upvotes,thumbnailURL,imageBitmap,imagePath)
        dataList.add(rData)
        Log.e("Main TITLE", "$i:${rData.toString()}")
    }
    return dataList

}
private fun saveImage(image: Bitmap,imageFileName:String,activity:FeedNetworkDisplayer): String? {
    var savedImagePath: String? = null
    val storageDir = File(
        activity.getParentContext().applicationInfo.dataDir + imageDirectory
    )
    var success = true
    if (!storageDir.exists()) {
        success = storageDir.mkdirs()
    }
    if (success) {
        val imageFile = File(storageDir, imageFileName)
        savedImagePath = imageFile.getAbsolutePath()
        var fOut: OutputStream? = null
        try {
            fOut = FileOutputStream(imageFile)
            image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally{
            fOut?.close()
        }
    }
    return savedImagePath
}
private fun getImageNameFromURL(url:String):String{
    val sb = StringBuilder()
    for(i in (0 until url.length).reversed()){
        if(url[i] == '/')
            break;
        sb.insert(0,url[i])
    }
    return sb.toString()
}

fun fetchRedditData(limit: Int, t:REDDIT_T, count:Int = 0, before: String = "", after:String = "", activity:FeedDatabaseDisplayer){
    var dataArray = ArrayList<RedditData>()
    var jsonResponse: JsonObject;
    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RedditRequest::class.java)
    val url = getRequestURL(limit,t,count,before,after)

    GlobalScope.launch(Dispatchers.IO){
        try{
            jsonResponse = api.getRedditTop(url)
            val after = jsonResponse["data"].asJsonObject["after"].toString().trim('"')

            //Save after to shared prefs
            setRedditAfterPreferences(activity.getParentContext(),after)
            dataArray = getDataFromJson(jsonResponse,activity)
            withContext(Dispatchers.Main){
                activity.onNetworkLoadFinished(dataArray);
            }

        }
        catch (e: Exception){
            Log.e("Main","Error: ${e.message}")
            withContext(Dispatchers.Main){
                activity.onNetworkError(e.message.toString());
            }
        }
    }
}
