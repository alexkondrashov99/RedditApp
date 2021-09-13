package com.example.data.datasource.redditpostdatasourceimpl

import com.example.data.datasource.redditpostdatasourceinterface.RedditPostRemoteDataSource
import android.util.Log
import com.example.data.Utils
import com.example.data.Utils.getBitmapFromURL
import com.example.domain.models.AfterInfo
import com.example.domain.models.REDDIT_T
import com.example.domain.models.RedditPost

import com.example.data.datasource.redditpostdatasourceinterface.*
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


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

interface RedditPostService {
    @GET("/top.json?/")
    suspend fun getRedditPostTopList(
        @Query("limit")  limit: Int,
        @Query("t")  t: String,
        @Query("after")  after: String
    ): JsonObject
}

class RedditPostRemoteDataSourceImpl: RedditPostRemoteDataSource {

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Throws(java.net.UnknownHostException::class)
    override suspend fun fetchRedditPostList(
        limit: Int,
        t: REDDIT_T,
        count: Int,
        before: String,
        afterInfo: AfterInfo
    ): RedditPostRemoteDataSource.FetchedData = withContext(Dispatchers.Default) {

        var redditPostDataList:List<RedditPost>
        var after: AfterInfo
        //fetching from WEB...
        val jsonResponse = retrofit
            .create(RedditPostService::class.java)
            .getRedditPostTopList(limit,t.value,afterInfo.after)

        //mapping  JSON to RedditPostData
        redditPostDataList = jsonResponse.toRedditPostList()

        //saving after info
        after = AfterInfo(jsonResponse["data"]
            .asJsonObject["after"]
            .toString()
            .trim('"'))
        /*return*/
        RedditPostRemoteDataSource.FetchedData(redditPostDataList,after)
    }

}


private fun JsonObject.toRedditPostList(): List<RedditPost> {
    val count = this["data"].asJsonObject["children"].asJsonArray.size();
    val dataList = ArrayList<RedditPost>()
    val jsonArr = this["data"].asJsonObject["children"].asJsonArray
    for(i in 0 until count){
        val data = jsonArr[i].asJsonObject["data"].asJsonObject
        val title = data[json_title].toString().trim('"')
        val author = data[json_author].toString().trim('"')
        val createdUTC = data[json_created_utc].toString().toDouble().toLong()
        val numComments = data[json_num_comments].toString().toInt()
        val url = data[json_url].toString().trim('"')
        val upvotes = data[json_upvotes].toString().toInt()
        val subreddit = data[json_subreddit].toString().trim('"')
        var thumbnailURL = data[json_thumbnail].toString().trim('"')

        if(getBitmapFromURL(thumbnailURL) == null) {
            thumbnailURL = "null"
        }
        val rData = RedditPost(title,author,createdUTC,numComments,url,subreddit,upvotes,thumbnailURL)
        dataList.add(rData)
    }
    return dataList

}



