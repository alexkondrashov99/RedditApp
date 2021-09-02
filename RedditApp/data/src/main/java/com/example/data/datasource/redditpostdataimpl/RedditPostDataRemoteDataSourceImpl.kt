package com.example.data.datasource.redditpostdataimpl

import com.example.data.datasource.models.AfterInfoData
import com.example.data.datasource.models.RedditPostData
import com.example.data.datasource.redditpostdatainterface.RedditPostDataRemoteDataSource
import com.example.data.datasource.redditpostdatainterface.RedditPostDataRemoteDataSource.REDDIT_T

class RedditPostDataRemoteDataSourceImpl:RedditPostDataRemoteDataSource {

    private val after: AfterInfoData? = null

    override suspend fun fetchRedditPostList(
        limit: Int,
        t: REDDIT_T,
        count: Int,
        before: String,
        after: AfterInfoData
    ): List<RedditPostData> {

        TODO("Retrofit???")
    }

    override suspend fun getAfterInfo(): AfterInfoData? {
        return after
    }
}