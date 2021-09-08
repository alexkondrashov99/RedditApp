package com.example.data.repository


import com.example.data.datasource.redditpostdatasourceinterface.RedditPostLocalDataSource
import com.example.data.datasource.redditpostdatasourceinterface.RedditPostRemoteDataSource

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.example.domain.models.REDDIT_T
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class RedditPostRepositoryImpl (
    private val localDataSource: RedditPostLocalDataSource,
    private val remoteDataSource: RedditPostRemoteDataSource
) : RedditPostRepository {

    companion object {
        // For Singleton instantiation
        @Volatile private var instance: RedditPostRepository? = null
        fun getInstance(redditPostLocalDataSource: RedditPostLocalDataSource, redditPostRemoteDataSource: RedditPostRemoteDataSource ) =
            instance ?: synchronized(this) {
                instance ?: RedditPostRepositoryImpl(redditPostLocalDataSource, redditPostRemoteDataSource).also { instance = it }
            }
    }

    override suspend fun fetchRemoteRedditPostList(limit: Int, t: REDDIT_T, count:Int, before: String, after: AfterInfo) {
        val fetchedData = remoteDataSource.fetchRedditPostList(limit, t, count, before, after)
        val redditPostDataList: List<RedditPost> = fetchedData.redditPostList
        val afterInfo: AfterInfo = fetchedData.afterInfo

        //saving fetched data to local database
        localDataSource.refreshRedditPostList(redditPostDataList)
        localDataSource.setRedditAfterInfo(afterInfo)
    }

    override suspend fun updateRedditPostList(limit: Int, t: REDDIT_T, count:Int, before: String, after: AfterInfo) {
        val fetchedData = remoteDataSource.fetchRedditPostList(limit, t, count, before, after)
        val redditPostDataList: List<RedditPost> = fetchedData.redditPostList
        val afterInfo: AfterInfo = fetchedData.afterInfo

        //saving fetched data to local database
        localDataSource.updateRedditPostList(redditPostDataList)
        localDataSource.setRedditAfterInfo(afterInfo)
    }

    override fun observeRedditPostList(): Flow<List<RedditPost>> {
        return localDataSource.observeRedditPost()
    }

    override fun observeRedditAfterInfo(): Flow<AfterInfo> {
        return localDataSource.observeAfterInfo()
    }

    override suspend fun getRedditAfterInfo(): AfterInfo {
        return localDataSource.getRedditAfterInfo()
    }

    override suspend fun getRedditPostList(): List<RedditPost> {
        return localDataSource.getRedditPostList()
    }

}


