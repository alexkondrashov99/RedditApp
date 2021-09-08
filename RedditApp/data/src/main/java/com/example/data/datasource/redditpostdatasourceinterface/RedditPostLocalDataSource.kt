package com.example.data.datasource.redditpostdatasourceinterface


import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import kotlinx.coroutines.flow.Flow



interface RedditPostLocalDataSource {

    suspend fun refreshRedditPostList(list: List<RedditPost>)
    suspend fun updateRedditPostList(list: List<RedditPost>)
    suspend fun setRedditAfterInfo(afterInfoData: AfterInfo)

    fun observeRedditPost(): Flow<List<RedditPost>>
    fun observeAfterInfo(): Flow<AfterInfo>

    suspend fun getRedditPostCount(): Int
    suspend fun getRedditPostList(): List<RedditPost>
    suspend fun getRedditAfterInfo(): AfterInfo
}
