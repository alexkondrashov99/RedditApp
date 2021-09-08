package com.example.data.datasource.redditpostdatainterface

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost

interface RedditPostRemoteDataSource {

    enum class REDDIT_T(val value:String){
        HOUR("hour"),
        DAY("day"),
        WEEK("week"),
        MONTH("month"),
        YEAR("year"),
        ALL("all")
    }

    suspend fun fetchRedditPostList(limit: Int, t:REDDIT_T, count:Int, before: String, after: AfterInfo): List<RedditPost>
    suspend fun getAfterInfo(): AfterInfo?
}
