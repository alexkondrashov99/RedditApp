package com.example.data.datasource.redditpostdatainterface


import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import kotlinx.coroutines.flow.Flow



interface RedditPostLocalDataSource {
    suspend fun getRedditPostCount(): Int

    suspend fun getRedditPostList(): List<RedditPost>

    suspend fun refreshRedditPostList(list: List<RedditPost>)

    suspend fun updateRedditPostList(list: List<RedditPost>)

    suspend fun setRedditAfterInfo(afterInfoData: AfterInfo)

    suspend fun getRedditAfterInfo(): AfterInfo

    suspend fun clearRedditAfterInfo()

    suspend fun observeRedditPost(): Flow<List<RedditPost>>

    suspend fun observeAfterInfo(): Flow<AfterInfo>
}
