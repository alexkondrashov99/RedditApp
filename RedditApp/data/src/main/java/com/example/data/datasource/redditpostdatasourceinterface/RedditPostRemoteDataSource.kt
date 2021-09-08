package com.example.data.datasource.redditpostdatasourceinterface

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.models.REDDIT_T

interface RedditPostRemoteDataSource {

    suspend fun fetchRedditPostList(limit: Int, t: REDDIT_T, count:Int, before: String, after: AfterInfo): FetchedData
    data class FetchedData(val redditPostList: List<RedditPost>, val afterInfo: AfterInfo)
}
