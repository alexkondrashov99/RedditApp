package com.example.data.datasource.redditpostdatainterface

import com.example.data.datasource.models.AfterInfoData
import com.example.data.datasource.models.RedditPostData

interface RedditPostDataRemoteDataSource {

    enum class REDDIT_T(val value:String){
        HOUR("hour"),
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        YEAR("year"),
        ALL("all")
    }

    suspend fun fetchRedditPostList(limit: Int, t:REDDIT_T, count:Int, before: String, after:AfterInfoData): List<RedditPostData>
    suspend fun getAfterInfo(): AfterInfoData?
}
